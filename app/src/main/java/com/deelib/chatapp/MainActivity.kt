package com.deelib.chatapp

import android.R
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deelib.chatapp.model.ChatData
import com.deelib.chatapp.ui.theme.ChatAppTheme
import com.deelib.chatapp.utils.Utils
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    val utils = Utils()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val chatData = utils.loadMessage(this)
        setContent {
            ChatAppTheme {
                ChatPage(chatData)
            }
        }
    }
}

@Composable
fun ChatPage(chatData: List<ChatData>) {
    val messageList = remember {
        mutableStateListOf<ChatData>().apply {
            addAll(chatData)
        }
    }
    var newText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    Surface(
        modifier = Modifier
            .fillMaxSize(), color = Color.Black
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                reverseLayout = true
            ) {
                items(messageList.reversed(), key = { it.chatId }) { message ->
                    ChatBubble(message)

                }

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                TextField(
                    value = newText,
                    onValueChange = { newText = it },
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(15.dp),
                            color = Color.LightGray
                        ),
                    shape = RoundedCornerShape(15.dp),
                    singleLine = true,
                    placeholder = { Text(text = "Send a Message") })

                IconButton(onClick = {
                    if (newText.isNotBlank()) {
                        messageList.add(
                            ChatData(
                                chatId = Random.nextInt(100),
                                message = newText,
                                time = "Today",
                                sender = 1
                            )
                        )
                        newText = ""
                    }
                }) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Color(0xFF2E696A),
                        modifier = Modifier.size(20.dp)
                    )
                }


            }

            Spacer(modifier = Modifier.size(20.dp))
        }
    }


}

@Composable
fun ChatBubble(chatData: ChatData) {
    val isUser = chatData.sender == 1
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            color = if (isUser) Color(0xFF2E696A) else Color(0xFF273128),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .padding(horizontal = 5.dp)
//                .widthIn(max = 0.7f.times( LocalConfiguration.current.screenWidthDp.dp))
        ) {
            Text(
                text = chatData.message,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 5.dp, bottom = 5.dp),
                fontSize = 16.sp,
                color = Color.White

            )
        }

    }

}



