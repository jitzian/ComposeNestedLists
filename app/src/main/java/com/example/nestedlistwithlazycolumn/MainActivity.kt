package com.example.nestedlistwithlazycolumn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nestedlistwithlazycolumn.ui.theme.NestedListWithLazyColumnTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NestedListWithLazyColumnTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ComposePreview()
                }
            }
        }
    }
}

@Composable
@Preview
fun ComposePreview() {
    NestedListWithLazyColumnTheme {
        var questionExpanded by remember {
            mutableStateOf<Question?>(null)
        }
        LazyColumn {
            questions.forEach { question ->
                val expanded = question == questionExpanded
                item {
                    QuestionItem(
                        question = question,
                        expanded = expanded,
                        onExpandedChange = {
                            questionExpanded = if (question == questionExpanded) {
                                null
                            } else {
                                question
                            }
                        }
                    )
                }
                if (expanded) {
                    items(question.subQuestions) { subQuestion ->
                        SubQuestionItem(question.id, subQuestion)
                    }
                }
            }
        }
    }
}

@Composable
private fun SubQuestionItem(parentId: Int, subQuestion: SubQuestion) {
    Column(
        modifier = Modifier
            .padding(all = 8.dp)
            .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
            .background(Color.LightGray, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Row {
            Text(
                text = "$parentId.${subQuestion.id} ${subQuestion.text}",
                modifier = Modifier.weight(1f)
            )
            Icon(imageVector = Icons.Default.QuestionAnswer, contentDescription = null)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Default.PhotoCamera, contentDescription = null)
            Icon(imageVector = Icons.Default.Comment, contentDescription = null)
            ActionButtons()
        }
    }
}

@Composable
fun QuestionItem(question: Question, expanded: Boolean, onExpandedChange: () -> Unit) {
    Column(modifier = Modifier.padding(all = 8.dp)) {
        Row {
            Text(text = "${question.id} ${question.text}", modifier = Modifier.weight(1f))
            Icon(imageVector = Icons.Default.QuestionAnswer, contentDescription = null)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Apply all")
            ActionButtons()
            IconButton(onClick = onExpandedChange) {
                val icon = if (expanded) {
                    Icons.Default.ArrowDropUp
                } else {
                    Icons.Default.ArrowDropDown
                }
                Icon(imageVector = icon, contentDescription = null)
            }
        }
    }
}

@Composable
private fun ActionButtons() {
    CustomButton("OK")
    CustomButton("NOK")
    CustomButton("N/A")
}

@Composable
private fun CustomButton(text: String) {
    OutlinedButton(onClick = { /*TODO*/ }, contentPadding = PaddingValues(4.dp)) {
        Text(text = text)
    }
}

data class Question(val id: Int, val text: String, val subQuestions: List<SubQuestion>)
data class SubQuestion(val id: Int, val text: String)

val questions = buildList {
    (1..20).forEach { questionId ->
        add(
            Question(
                id = questionId,
                text = "Question $questionId",
                subQuestions = (1..20).map {
                    SubQuestion(
                        it,
                        "SubQuestion $questionId.$it"
                    )
                }
            )
        )
    }
}
