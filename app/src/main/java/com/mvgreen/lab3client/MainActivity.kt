package com.mvgreen.lab3client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.mvgreen.data.network.Api
import com.mvgreen.data.network.ApiFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import java.lang.IllegalArgumentException

class MainActivity : AppCompatActivity() {

    private lateinit var api: Api
    private var id: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
    }

    private fun setupView() {
        submit_ip.setOnClickListener {
            try {
                val newIp = edit_ip.text.toString()
                api = ApiFactory.buildApi(newIp)
            } catch (e: IllegalArgumentException) {
                Log.e("App", e.message, e)
                Snackbar.make(it, e.message ?: "Проверьте корректность адреса", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            submit_ip.isEnabled = false
            edit_ip.isEnabled = false
            btn_register.isEnabled = true
        }

        btn_register.setOnClickListener {
            api
                .register()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { response, err ->
                    if (err != null) {
                        Log.e("App", err.message, err)
                        Snackbar
                            .make(it, err.message ?: "При регистрации произошла ошибка", Snackbar.LENGTH_LONG)
                            .show()
                        return@subscribe
                    }

                    if (response.status != null) {
                        Snackbar
                            .make(it, response.status!!, Snackbar.LENGTH_LONG)
                            .show()
                    }

                    if (response.id != null) {
                        id = response.id!!
                        if (id == -1) {
                            player_id.text = "Регистрация на сервере завершена"
                        } else {
                            player_id.text = id.toString()
                            btn_register.isEnabled = false
                            update_status.isEnabled = true
                        }
                    }
                }
        }

        update_status.setOnClickListener {
            api
                .update(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { response, err ->
                    if (err != null) {
                        Log.e("App", err.message, err)
                        Snackbar
                            .make(it, err.message ?: "При обновлении произошла ошибка", Snackbar.LENGTH_LONG)
                            .show()
                        return@subscribe
                    }

                    if (response.status != null) {
                        Snackbar
                            .make(it, response.status!!, Snackbar.LENGTH_LONG)
                            .show()
                    }

                    tv_current_player.text = response.currentPlayer?.toString() ?: ""
                    prev_line.text = response.previousLine ?: ""
                    result_poem.text = response.completePoem ?: ""
                    if (response.currentPlayer == id) {
                        btn_submit_line.isEnabled = true
                    }
                }
        }

        btn_submit_line.setOnClickListener {
            api.submit(id, edit_submission.text.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { response, err ->
                    if (err != null) {
                        Log.e("App", err.message, err)
                        Snackbar
                            .make(it, err.message ?: "При обновлении произошла ошибка", Snackbar.LENGTH_LONG)
                            .show()
                        return@subscribe
                    }

                    if (response.status != null) {
                        Snackbar
                            .make(it, response.status!!, Snackbar.LENGTH_LONG)
                            .show()
                    }
                }
        }
    }

}
