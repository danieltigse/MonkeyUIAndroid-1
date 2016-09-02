package com.criptext.monkeykitui.recycler

import android.graphics.Bitmap
import android.util.Log
import android.view.View
import com.criptext.monkeykitui.recycler.MonkeyAdapter
import java.util.*

/**
 * Created by gesuwall on 4/4/16.
 */

interface MonkeyItem {

    /*COMMON*/

    /**
     * @return the timestamp according with the server.
     * &nbsp{@see MonkeyAdapter}&nbsp will convert this value to a human readable string and
     * &nbsp\ display it to the user.
     *
     */
    fun getMessageTimestamp() : Long

    /**
     * @return the timestamp with the date in which the message was created.
     * &nbsp{@see MonkeyAdapter}&nbsp will convert this value to a human readable string and
     * &nbsp\ display it to the user.
     *
     */
    fun getMessageTimestampOrder() : Long

    /**
     * @return a unique identifier for this item.
     */
    fun getMessageId() : String

    /**
     * @return server or old identifier for this item.
     */
    fun getOldMessageId() : String

    /**
     * @return true if the message was received or false if it was sent by the user. This decides the
     * &nbsp\ style and alignment of the bubble.
     */
    fun isIncomingMessage() : Boolean

    /**
     * @return the status of a message sent by the user. See {@see OutgoingMessageStatus}
     * &nbsp\ for possible values
     */
    fun getDeliveryStatus() : DeliveryStatus

    /**
     * @return an integer with the ordinal value of the {@see MonkeyItemType} of this message.
     */
    fun getMessageType() : Int

    /*TEXT*/

    /**
     * @return A string with the message's text. Only used for messages of type text.
     */
    fun  getMessageText() : String

    /*PHOTO*/

    /**
     * @return A string with the path to a bitmap that can be decoded and used as a low resolution
     * &nbsp\ thumbnail while the real photo is being downloaded. Only used for messages of type photo.
     */
    fun getPlaceholderFilePath() : String

    /*AUDIO, PHOTO, FILE */

    /**
     * @return A string with the path to the file sent with this message. Only used for messages of
     * &nbsp\ type audio, photo and file.&nbsp{@see MonkeyAdapter} will decide how to display the file
     * using getMessageType(). Only used for messages of type audio, photo and file.
     */
    fun getFilePath() : String

    /**
     * @return a long with the size of the file in bytes. It is recommended to have this value cached
     * &nbsp\ instead of measuring it every time it is needed.
     */
    fun getFileSize() : Long

    /* AUDIO */

    /**
     * @return a Long with the duration of the audio file
     * &nbsp\ It is recommended to have this value cached instead of measuring it every time it is needed.
     * &nbsp\ only used for messages of type audio.
     */
    fun getAudioDuration() : Long

    /*CONTACT */

    fun getContactSessionId() : String

    companion object {

        /**
         * Compares two MonkeyItem elements. Start by comparing the order timestamp. If they are equal, it
         * falls back to comparing ID's which should be unique.
         */
        val defaultComparator = Comparator<MonkeyItem> { t1, t2 ->
            if (t1.getMessageTimestampOrder() < t2.getMessageTimestampOrder()) {
                -1
            } else if (t1.getMessageTimestampOrder() > t2.getMessageTimestampOrder()) {
                1
            } else t1.getMessageId().compareTo(t2.getMessageId())
        }

        fun findItemPositionInList(monkeyItem: MonkeyItem, list: List<MonkeyItem>) =
                list.binarySearch(monkeyItem, defaultComparator)

        fun findItemPositionInList(id: String, dateorder: Long, list: List<MonkeyItem>): Int {
            val searchItem = object : MonkeyItem {
                override fun getAudioDuration() = 0L
                override fun getDeliveryStatus() = DeliveryStatus.sending
                override fun getContactSessionId() = ""
                override fun getFileSize() = 0L
                override fun getFilePath() = ""
                override fun getMessageId() = id
                override fun getMessageText() = ""
                override fun getMessageTimestamp() = dateorder
                override fun getMessageTimestampOrder() = dateorder
                override fun getMessageType() = 0
                override fun getOldMessageId() = ""
                override fun getPlaceholderFilePath() = ""
                override fun isIncomingMessage() = true
            }
            return findItemPositionInList(searchItem, list)
        }
    }

    /**
     * enum class that holds the state of a message sent by the user. {@see MonkeyAdapter} will display
     * &nbsp\ the message bubble in a way that the user can understand the status of the sent message.
     */
    enum class DeliveryStatus {
        /* The message was just sent to the user, but it hasn't reached the server yet */
        sending,
        /* There was an error sending the message */
        error,
        /* The message has been delivered to the server, but the recipient hasn't read it yet */
        delivered,
        /* The message has been read by the recipient */
        read;

        fun isTransferring () = this < delivered


    }

    /**
     * enum class that holds the type of a message or element that will be displayed in the RecyclerView.
     */
    enum class MonkeyItemType() {
        text, audio, photo, contact, file, MoreMessages
    }


}