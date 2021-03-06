package com.criptext.monkeykitui.util

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.criptext.monkeykitui.R
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by gesuwall on 4/6/16.
 */

class Utils {
    companion object {

        val TAG = "MONKEY-UI-KIT"

        fun getHoraVerdadera(timestamp: Long) : String{
            val fechaPaelUser = SimpleDateFormat("h:mm a").format(timestamp).toUpperCase()
            return fechaPaelUser.replace("P.M.".toRegex(), "PM").replace("A.M.".toRegex(), "AM")
        }

        fun isTheSameDay(timestampActual: Long, timestampAnterior: Long): Boolean {

            val fechaAct = java.util.Date(timestampActual)
            val fechaAnt = java.util.Date(timestampAnterior)
            val fechaActual = Calendar.getInstance()
            val fechaAnterior = Calendar.getInstance()
            fechaActual.time = fechaAct
            fechaAnterior.time = fechaAnt

            val diferenciaDias = Math.abs(fechaActual.get(Calendar.DAY_OF_MONTH) - fechaAnterior.get(Calendar.DAY_OF_MONTH))
            val diferenciaMeses = fechaActual.get(Calendar.MONTH) - fechaAnterior.get(Calendar.MONTH)

            if (diferenciaMeses == 0) {
                if (diferenciaDias == 0) {
                    return true
                } else
                    return false
            }

            return false
        }

        fun getFormattedDay(timestamp: Long, context: Context?): String {

            val fechaMsj = java.util.Date(timestamp)
            val fechaAct = java.util.Date()
            var fechaPaelUser = ""
            val fechaMensaje = Calendar.getInstance()
            val fechaActual = Calendar.getInstance()
            fechaMensaje.time = fechaMsj
            fechaActual.time = fechaAct

            var strAyer = "Yesterday"
            var strHoy = "Today"
            if (context != null) {
                strAyer = context.resources.getString(R.string.mk_label_yesterday)
                strHoy = context.resources.getString(R.string.mk_label_today)
            }

            val diferenciaDias = Math.abs(fechaMensaje.get(Calendar.DAY_OF_MONTH) - fechaActual.get(Calendar.DAY_OF_MONTH))
            val diferenciaMeses = fechaMensaje.get(Calendar.MONTH) - fechaActual.get(Calendar.MONTH)

            if (diferenciaMeses == 0) {
                if (diferenciaDias == 0) {
                    fechaPaelUser = strHoy
                } else if (diferenciaDias == 1 || diferenciaDias == -1) {
                    fechaPaelUser = strAyer
                } else if (diferenciaDias < 7 && diferenciaDias > 0) {
                    fechaPaelUser = SimpleDateFormat("EEEE").format(timestamp)
                    fechaPaelUser = fechaPaelUser.substring(0, 1).toUpperCase() + fechaPaelUser.substring(1).toLowerCase()
                } else {
                    fechaPaelUser = SimpleDateFormat("MM/dd/yy").format(timestamp)
                }
            } else
                fechaPaelUser = SimpleDateFormat("MM/dd/yy").format(timestamp)

            return fechaPaelUser
        }

        fun getFormattedDate(timestamp: Long, context: Context?): String {

            val fechaMsj = java.util.Date(timestamp)
            val fechaAct = java.util.Date()
            var fechaPaelUser = ""
            val fechaMensaje = Calendar.getInstance()
            val fechaActual = Calendar.getInstance()
            fechaMensaje.time = fechaMsj
            fechaActual.time = fechaAct

            var strAyer = "Yesterday"
            if (context != null)
                strAyer = context.resources.getString(R.string.mk_label_yesterday)

            val diferenciaDias = Math.abs(fechaMensaje.get(Calendar.DAY_OF_MONTH) - fechaActual.get(Calendar.DAY_OF_MONTH))
            val diferenciaMeses = fechaMensaje.get(Calendar.MONTH) - fechaActual.get(Calendar.MONTH)

            if (diferenciaMeses == 0) {
                if (diferenciaDias == 0) {
                    fechaPaelUser = SimpleDateFormat("h:mm a").format(timestamp).toUpperCase()
                    fechaPaelUser = fechaPaelUser.replace("P.M.".toRegex(), "PM")
                    fechaPaelUser = fechaPaelUser.replace("A.M.".toRegex(), "AM")
                } else if (diferenciaDias == 1 || diferenciaDias == -1) {
                    fechaPaelUser = strAyer
                } else if (diferenciaDias < 7 && diferenciaDias > 0) {
                    fechaPaelUser = SimpleDateFormat("EEEE").format(timestamp)
                    fechaPaelUser = fechaPaelUser.substring(0, 1).toUpperCase() + fechaPaelUser.substring(1).toLowerCase()
                } else {
                    fechaPaelUser = SimpleDateFormat("MM/dd/yy").format(timestamp)
                }
            } else
                fechaPaelUser = SimpleDateFormat("MM/dd/yy").format(timestamp)

            return fechaPaelUser
        }


        /**
         * Adds a RecyclerView.LayoutParams to a view
         * @param view view to set the new layout params
         * @return the view with a RecyclerView.LayoutParams object as its layout params
         *
         */
        fun getViewWithRecyclerLayoutParams(view: View) : View {
            val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            view.layoutParams = RecyclerView.LayoutParams(params)
            return view
        }

        /**
         * Download avatar image with file cache and mem cache.
         * @param context contexto donde me encuentro
         * *
         * @param iv ImageView donde colocar el avatar
         * *
         * @param url url de donde descargar el avatar.
         * *
         * @param defaultUser true si el fallback debe de ser la imagen default user, de lo contrario usar
         * *                    default group como fallback
         */
        fun setAvatarAsync(context: Context, iv: ImageView, url: String?, isPersonalConv: Boolean, runnable: Runnable?) {

            val fallback_id = if (isPersonalConv) R.drawable.mk_default_user_img else R.drawable.mk_default_group_avatar
            if(url?.length==0) {
                iv.setImageResource(fallback_id)
                return
            }

            Picasso.with(context)
                    .load(url)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .resize(120, 120)
                    .centerCrop()
                    .placeholder(fallback_id)
                    .error(fallback_id)
                    .into(iv, object : Callback {

                        override fun onSuccess() {
                            runnable?.run()
                        }

                        override fun onError() {
                            Picasso.with(context)
                                    .load(url)
                                    .resize(120, 120)
                                    .centerCrop()
                                    .placeholder(fallback_id)
                                    .error(fallback_id)
                                    .into(iv)
                        }
                    })
        }
    }
    enum class ConnectionStatus {
        disconnected, connected, connecting, waiting_for_network;
    }
}
