package com.example.expensetracker.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.expensetracker.R
import com.example.expensetracker.data.model.ExpenseEntity
import com.example.expensetracker.widget.ExpenseText
import com.example.expensetracker.ui.theme.Zinc


@Composable
fun HomeScreen() {

    val viewModel:HomeScreenViewModel = HomeViewModelFactory(LocalContext.current).create(HomeScreenViewModel::class.java)
    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, list, card, topBar, add) = createRefs()
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
                .padding(top = 64.dp, start = 16.dp, end = 16.dp)
                .constrainAs(nameRow) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                Column(modifier = Modifier.align(Alignment.CenterStart)) {
                    ExpenseText(text = "Hello,", fontSize = 18.sp,color = Color.White)
                    ExpenseText(text = "Abhinn Agrawal", fontSize = 24.sp,color = Color.White, fontWeight = FontWeight.Bold)
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_notification),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
            val state = viewModel.expenses.collectAsState(initial = emptyList())
            val balance = viewModel.getBalance(state.value)
            val expenses = viewModel.getTotalExpense(state.value)
            val income = viewModel.getTotalIncome(state.value)
            CardItem(
                modifier = Modifier.constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                balance,expenses,income
            )
            TransactionList(modifier = Modifier
                .fillMaxWidth()
                .constrainAs(list) {
                    top.linkTo(card.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                },
                state.value,viewModel
            )
        }
    }
}



@Composable
fun TransactionList(modifier: Modifier,list : List<ExpenseEntity>,viewModel: HomeScreenViewModel) {
    LazyColumn(modifier = modifier.padding(horizontal = 16.dp)) {
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                ExpenseText(text = "Recent Transactions", fontSize = 20.sp)
                ExpenseText(
                    text = "See All",
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
        }
        items(list){item ->
            TransactionItem(
                title = item.title,
                amount = item.amount.toString(),
                icon = viewModel.getItemIcon(item),
                date = item.date.toString() ,
                color = if(item.type == "Income") Color.Green else Color.Red,
                Modifier
            )
        }
    }
}

@Composable
fun TransactionItem(
    title: String,
    amount: String,
    icon: Int,
    date: String,
    color: Color,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(modifier = Modifier.align(Alignment.CenterStart),verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column {
                ExpenseText(text = title, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.size(2.dp))
                ExpenseText(text = date, fontSize = 14.sp)
            }
        }
        Row(modifier = Modifier.align(Alignment.CenterEnd),verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.baseline_currency_rupee_24),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.size(3.dp))
            ExpenseText(text = amount, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = color)
        }
    }
}

@Composable
fun CardItem(modifier: Modifier,balance:String, expenses:String, income:String) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .shadow(16.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Zinc)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column {
                ExpenseText(text = "Total Balance", color = Color.White, fontSize = 20.sp)
                Spacer(modifier = Modifier.size(4.dp))
                Amount(amount = balance , color = Color.White,size = 22,bold = true)
            }
            Image(
                painter = painterResource(id = R.drawable.dots_menu),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            CardRowItem(
                modifier = Modifier.align(Alignment.CenterStart),
                title = "Income",
                amount = income,
                image = R.drawable.ic_income
            )
            Spacer(modifier = Modifier.size(8.dp))
            CardRowItem(
                modifier = Modifier.align(Alignment.CenterEnd),
                title = "Expense",
                amount = expenses,
                image = R.drawable.ic_expense
            )
        }
    }
}

@Composable
fun CardRowItem(modifier: Modifier, title: String, amount: String, image: Int) {
    Column(modifier = modifier) {
        Row {

            Image(
                painter = painterResource(id = image),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.size(8.dp))
            ExpenseText(text = title, fontSize = 18.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.size(4.dp))
        Amount(amount = amount, color = Color.White, size = 20, bold = false)
    }
}

@Composable
fun Amount(amount:String,color:Color,size:Int,bold:Boolean = false){
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(painter = painterResource(id = R.drawable.baseline_currency_rupee_24),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color),
            modifier = Modifier.size(size.dp)
        )
        Spacer(modifier = Modifier.size(3.dp))
        ExpenseText(text = amount, color = color,fontSize = size.sp,fontWeight = if(bold) FontWeight.Bold else null)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}