package com.criptext.monkeykitui.info.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.criptext.monkeykitui.R
import com.criptext.monkeykitui.util.Utils
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by hirobreak on 04/10/16.
 */

open class InfoHolder: RecyclerView.ViewHolder {

    val nameTextView: TextView?
    val secondaryTextView: TextView?
    val tagTextView: TextView?
    val avatarImageView: CircleImageView?

    constructor(view : View, textMaxWidth: Int) : super(Utils.getViewWithRecyclerLayoutParams(view)) {
        nameTextView = view.findViewById(R.id.info_name) as TextView?
        secondaryTextView = view.findViewById(R.id.info_secondary_txt) as TextView?
        tagTextView = view.findViewById(R.id.info_rol) as TextView?
        avatarImageView = view.findViewById(R.id.info_avatar) as CircleImageView?

        nameTextView?.maxWidth = textMaxWidth
        secondaryTextView?.maxWidth = textMaxWidth


    }

    open fun setName(name: String){
        nameTextView!!.text = name
    }

    open fun setSecondaryText(text: String){
        secondaryTextView!!.text = text
    }

    open fun setTag(tag: String){
        tagTextView!!.text = tag
    }

    open fun setAvatar(filepath: String?, isGroup: Boolean){
        val imageView = avatarImageView
        if(imageView != null) {
            if (filepath != null && filepath.length > 0)
                Utils.setAvatarAsync(imageView.context, imageView, filepath, !isGroup, null)
            else
                imageView.setImageResource(if (isGroup) R.drawable.mk_default_group_avatar else
                    R.drawable.mk_default_user_img)
        }
    }

    class EndHolder(view: View) : InfoHolder(view, 0);
}


