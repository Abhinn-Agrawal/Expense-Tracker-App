package com.example.expensetracker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.expensetracker.widget.ExpenseText

@Composable
fun AddExpenseScreen(){
    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow,card,topBar) = createRefs()
            Image(painter = painterResource(id = R.drawable.ic_topbar), contentDescription = null,
                modifier = Modifier.fillMaxWidth().constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                contentScale = ContentScale.Crop
            )
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp, start = 16.dp, end = 16.dp)
                .constrainAs(nameRow) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }){
                Image(painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                ExpenseText(text ="Add Expense",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
                Image(painter = painterResource(id = R.drawable.dots_menu),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
            DataForm(modifier = Modifier
                .padding(top = 60.dp)
                .constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
        }
    }
}

@Composable
fun DataForm(modifier:Modifier){
    Column(modifier = modifier
        .padding(16.dp)
        .fillMaxWidth()
        .shadow(16.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Color.White)
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
    ){
        ExpenseText(text = "Type", fontSize = 16.sp)
        OutlinedTextField(value = "", onValueChange ={}, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp))

        Spacer(modifier = Modifier.size(16.dp))
        ExpenseText(text = "Name", fontSize = 16.sp)
        OutlinedTextField(value = "", onValueChange = {}, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp))

        Spacer(modifier = Modifier.size(16.dp))
        ExpenseText(text = "Amount", fontSize = 16.sp)
        OutlinedTextField(value = "", onValueChange = {}, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp))

        Spacer(modifier = Modifier.size(16.dp))
        ExpenseText(text = "Category", fontSize = 16.sp)
        OutlinedTextField(value = "", onValueChange = {}, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp))

        Spacer(modifier = Modifier.size(16.dp))
        ExpenseText(text = "Date", fontSize = 16.sp)
        OutlinedTextField(value = "", onValueChange ={}, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp))

        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = { /*TODO*/ },modifier = Modifier.fillMaxWidth()) {
            ExpenseText(text = "Add Expense", fontSize = 16.sp)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AddExpensePreview(){
    AddExpenseScreen()
}
