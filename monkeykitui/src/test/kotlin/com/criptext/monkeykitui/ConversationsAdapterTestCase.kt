package com.criptext.monkeykitui

import android.app.Activity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.criptext.monkeykitui.conversation.ConversationsActivity
import com.criptext.monkeykitui.conversation.MonkeyConversation
import com.criptext.monkeykitui.conversation.MonkeyConversationsAdapter
import org.junit.Before
import org.robolectric.Robolectric
import java.util.*

/**
 * Created by gesuwall on 9/6/16.
 */
open class ConversationsAdapterTestCase {
lateinit var adapter: MonkeyConversationsAdapter
    var activity: Activity? = null
    var recycler: RecyclerView? = null


    @Before
    fun initAdapter(){
        if(activity == null){
            val newActivity = Robolectric.setupActivity(MonkeyActivity::class.java)
            adapter = MonkeyConversationsAdapter(newActivity)
            recycler = RecyclerView(newActivity);
            recycler!!.layoutManager = LinearLayoutManager(newActivity)
            activity = newActivity
        }
    }
    class MonkeyActivity: Activity(), ConversationsActivity {
        override fun onConversationClicked(conversation: MonkeyConversation) {
        }

        override fun onConversationDeleted(group: MonkeyConversation) {
        }

        override fun onLoadMoreConversations(loadedConversations: Int) {
        }

        override fun requestConversations() {
        }

        override fun retainConversations(conversations: List<MonkeyConversation>) {
        }

        override fun setConversationsFragment(conversationsFragment: MonkeyConversationsFragment?) {
        }

    }


}