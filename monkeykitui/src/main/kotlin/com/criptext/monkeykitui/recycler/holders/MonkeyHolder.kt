package com.criptext.monkeykitui.recycler.holders

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.criptext.monkeykitui.R
import com.criptext.monkeykitui.recycler.MonkeyItem
import com.criptext.monkeykitui.util.Utils
import java.util.*

/**
 * Created by gesuwall on 4/11/16.
 */
open class MonkeyHolder : RecyclerView.ViewHolder {
    /* COMMON */
    var datetimeTextView : TextView? = null
    var bubbleLayout : ViewGroup? = null
    var tailImageView : ImageView? = null
    var selectedImageView : ImageView? = null
    var dateSeparatorTextview: TextView? = null
    var layoutDateHeader: LinearLayout? = null
    /* COMMON OUTGOING */
    var checkmarkImageView : ImageView? = null
    var errorImageView : ImageView? = null
    var sendingProgressBar : ProgressBar? = null
    /* COMMON INCOMING */
    var senderNameTextView : TextView? = null
    var privateDatetimeTextView : TextView? = null
    var privacyCoverView : LinearLayout? = null
    var privacyTextView : TextView? = null

    /* CONTACT */
    var contactAvatarImageView : ImageView? = null
    var contactNameTextView : TextView? = null
    var createNewTextView : TextView? = null
    var addExisitingTextView : TextView? = null
    /* COMMON IMAGE & FILE & AUDIO */
    var filesizeTextView : TextView? = null

    constructor(view : View) : super(Utils.getViewWithRecyclerLayoutParams(view)) {
        datetimeTextView = view.findViewById(R.id.datetime) as TextView?
        bubbleLayout = view.findViewById(R.id.content_message) as ViewGroup?
        tailImageView = view.findViewById(R.id.tail) as ImageView?
        selectedImageView = view.findViewById(R.id.imageViewChecked) as ImageView?
        dateSeparatorTextview = view.findViewById(R.id.textViewDay) as TextView?
        layoutDateHeader = view.findViewById(R.id.layoutDateHeader) as LinearLayout?

        senderNameTextView = view.findViewById(R.id.sender_name) as TextView?

        checkmarkImageView = view.findViewById(R.id.imageViewCheckmark) as ImageView?
        errorImageView = view.findViewById(R.id.net_error) as ImageView?
        sendingProgressBar = view.findViewById(R.id.sendingWheel) as ProgressBar?
        sendingProgressBar?.indeterminateDrawable?.setColorFilter(Color.parseColor("#014766"), android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    /**
     * Changes this MonkeyHolder's UI so that it can inform the user about the
     * bound MonkeyItem's status, whether it has been read or delivered.
     *
     * @param status the status of the MonkeyItem bound to this holder
     */
    open fun updateReadStatus(status : MonkeyItem.DeliveryStatus, read: Boolean){
        if(status == MonkeyItem.DeliveryStatus.delivered){
            checkmarkImageView?.visibility = View.VISIBLE
            checkmarkImageView?.setImageDrawable(ContextCompat.getDrawable(
                    checkmarkImageView?.context, if(read) R.drawable.mk_checkmark_read else
                    R.drawable.mk_checkmark_sent));

        } else {
            checkmarkImageView?.visibility = View.GONE
        }
    }

    /**
     * Changes this MonkeyHolder's UI so that it can inform the user about the
     * bound MonkeyItem's sending status, whether it is still in route to server, or it has
     * already been successfully delivered.
     * @param status the status of the MonkeyItem bound to this holder
     * @param isOnline true if there's an active internet connection
     * @param messageTimestamp timestamp of the date that the message was sent.
     */
    open fun updateSendingStatus(status : MonkeyItem.DeliveryStatus, isOnline : Boolean, messageTimestamp: Long){
        if(status.isTransferring()){
            checkmarkImageView?.visibility = View.GONE
            if(status == MonkeyItem.DeliveryStatus.sending){
                errorImageView?.visibility = View.GONE
                sendingProgressBar?.visibility = View.VISIBLE
            }else{
                errorImageView?.visibility = View.VISIBLE
                sendingProgressBar?.visibility = View.GONE
            }
            //COMPARO TIMESTAMPS
            if((System.currentTimeMillis()/1000)- messageTimestamp >= 15){
                errorImageView?.visibility = View.VISIBLE
                sendingProgressBar?.visibility = View.GONE
            }
        }else if(!status.isTransferring()){
            sendingProgressBar?.visibility = View.GONE
            checkmarkImageView?.visibility = View.VISIBLE
            errorImageView?.visibility = View.GONE
        }
    }

    /**
     * Makes this MonkeyHolder display the name of the user that sent the bound
     * MonkeyItem.
     * @param name The name of the user
     * @param color The color that the text of the user's name should have.
     */
    open fun setSenderName(name : String, color : Int){
        senderNameTextView!!.visibility = View.VISIBLE
        senderNameTextView!!.text = name
        senderNameTextView!!.setTextColor(color)
    }

    open fun hideSenderName(){
        senderNameTextView!!.visibility = View.GONE
    }
    /**
     * Makes this MonkeyHolder display the date in which the the bound
     * MonkeyItem was sent.
     * @param timestamp a timestamp of the date in which the MonkeyItem was sent.
     */
    open fun setMessageDate(timestamp : Long){
        datetimeTextView!!.text = Utils.getHoraVerdadera(timestamp)
    }

    /**
     * Sets a long click listener to this MonkeyHolder.
     * @param listener The OnLongClickListener to set to this MonkeyHolder
     */
    open fun setOnLongClickListener(listener: View.OnLongClickListener){
        bubbleLayout?.setOnLongClickListener(listener)
    }

    open fun setSeparatorText(position: Int, item: MonkeyItem, messagesList: ArrayList<MonkeyItem>) {

        layoutDateHeader?.visibility = View.GONE

        if (position > 0) {
            val previousMessage = messagesList[position - 1]
            if (!Utils.isTheSameDay(item.getMessageTimestamp() * 1000, previousMessage.getMessageTimestamp() * 1000)) {
                dateSeparatorTextview?.text = Utils.getFormattedDay(item.getMessageTimestamp() * 1000, dateSeparatorTextview?.context)
                layoutDateHeader?.visibility = View.VISIBLE
            }
        } else if (position == 0) {
            dateSeparatorTextview?.text = Utils.getFormattedDay(item.getMessageTimestamp() * 1000, dateSeparatorTextview?.context)
            layoutDateHeader?.visibility = View.VISIBLE
        }

    }

    /**
     * Makes this MonkeyHolder display to the user whether the bound MonkeyItem
     * is selected or not.
     * @param isSelected true if the bound MonkeyItem is selected, otherwise false
     */
    open fun updateSelectedStatus(isSelected: Boolean){
        if(isSelected) {
            selectedImageView?.visibility = View.VISIBLE
            bubbleLayout?.alpha = 0.5f
            tailImageView?.alpha = 0.5f
        } else{
            selectedImageView?.visibility = View.INVISIBLE
            bubbleLayout?.alpha = 1f
            tailImageView?.alpha = 1f
        }
    }

    fun setBackgroundColor(color: Int){
        val bgShape = bubbleLayout!!.background.current as GradientDrawable
        bgShape.setColor(color)
    }

}