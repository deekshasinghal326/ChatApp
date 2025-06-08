package com.deelib.chatapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deelib.chatapp.model.ChatData
import com.deelib.chatapp.ui.theme.ChatAppTheme
import com.deelib.chatapp.utils.Utils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private val utils by lazy { Utils() }
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

    LaunchedEffect(messageList.size) {
        listState.animateScrollToItem(0)
    }

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
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = newText,
                    onValueChange = { newText = it.take(100) },
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(24.dp),
                            color = Color.LightGray
                        )
                        .padding(8.dp),
                    shape = RoundedCornerShape(15.dp),
                    maxLines = 1,
                    placeholder = { Text(text = "Send a Message") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                val currentTime: String =
                    SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                IconButton(onClick = {
                    if (newText.isNotBlank()) {
                        messageList.add(
                            ChatData(
                                chatId = UUID.randomUUID().hashCode(),
                                message = newText,
                                time = currentTime,
                                sender = 1
                            )
                        )
                        newText = ""
                    }
                }) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Send",
                        tint = if (newText.isBlank()) Color.Gray else Color(0xFF2E696A),
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 4.dp),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            color = if (isUser) Color(0xFF2E696A) else Color(0xFF273128),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .widthIn(max = (LocalConfiguration.current.screenWidthDp * 0.7f).dp)
        ) {
            Text(
                text = chatData.message,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(8.dp),
                fontSize = 16.sp,
                color = Color.White
            )
        }

        Text(
            text = chatData.time,
            fontSize = 8.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}




