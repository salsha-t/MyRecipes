package com.portfolio.myrecipes.ui

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.compose.material3.ExperimentalMaterial3Api
import com.bumptech.glide.Glide
import com.portfolio.myrecipes.R
import com.portfolio.myrecipes.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataName = intent.getStringExtra(EXTRA_NAME)
        val dataTime = intent.getStringExtra(EXTRA_TIME)
        val dataServing = intent.getStringExtra(EXTRA_SERVING)
        val dataIngredient = intent.getStringExtra(EXTRA_INGREDIENT)
        val dataStep = intent.getStringExtra(EXTRA_STEP)
        val dataPhoto = intent.getStringExtra(EXTRA_PHOTO)

        with(binding){
            tvName.text = dataName
            tvTime.text = dataTime
            tvServing.text = dataServing
            tvIngredient.text = dataIngredient
            tvStep.text = dataStep
            Glide.with(this@DetailActivity)
                .load(dataPhoto) // URL Gambar
                .into(ivPhoto) // imageView mana yang akan diterapkan
            btBack.setOnClickListener(this@DetailActivity)
            btShare.setOnClickListener(this@DetailActivity)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onClick(v: View) {
        when(v.id){
            R.id.bt_back -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.bt_share -> {
                val mBitmap = (binding.ivPhoto.drawable as BitmapDrawable).bitmap

                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, binding.tvName.text.toString())
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")

                    // Pada API 29 ke atas, digunakan IS_PENDING untuk menandai file yang masih dalam proses penulisan
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        put(MediaStore.Images.Media.IS_PENDING, 1)
                    }
                }

                // Mendapatkan URI untuk lokasi penyimpanan gambar
                val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

                uri?.let { imageUri ->
                    // Membuka output stream ke URI tersebut dan menyimpan bitmap sebagai JPEG
                    contentResolver.openOutputStream(imageUri).use { outputStream ->
                        if (outputStream != null) {
                            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        }
                    }

                    // Setelah penulisan selesai, update status IS_PENDING menjadi 0 agar gambar bisa terlihat di galeri
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        values.clear()
                        values.put(MediaStore.Images.Media.IS_PENDING, 0)
                        contentResolver.update(imageUri, values, null, null)
                    }
                }

                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Download aplikasi My Recipes dan dapatkan resep ${binding.tvName.text} sekarang juga!")
                shareIntent.type = "text/*"
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                shareIntent.type = "*image/*"
                startActivity(Intent.createChooser(shareIntent, null))
            }
        }
    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_TIME = "extra_time"
        const val EXTRA_SERVING = "extra_serving"
        const val EXTRA_INGREDIENT = "extra_ingredient"
        const val EXTRA_STEP = "extra_step"
        const val EXTRA_PHOTO = "extra_photo"
    }
}