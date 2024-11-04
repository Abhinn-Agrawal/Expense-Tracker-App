package com.example.expensetracker.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.R
import com.example.expensetracker.SharedViewModel
import com.example.expensetracker.navigation.ScreenRoute
import com.example.expensetracker.data.ExpenseEntity
import com.example.expensetracker.widget.ExpenseText
import com.example.expensetracker.ui.theme.Zinc
import com.example.expensetracker.widget.Utils


@Composable
fun HomeScreen(navController: NavController, sharedViewModel: SharedViewModel) {

    val viewModel:HomeScreenViewModel = HomeViewModelFactory(LocalContext.current).create(HomeScreenViewModel::class.java)
    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, list, card, topBar, add) = createRefs()
            Image(painter = painterResource(id = R.drawable.ic_topbar), contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(topBar) {
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
                    ExpenseText(text = "Hello,", fontSize = 18.sp,color = Color.White,fontWeight = FontWeight.Bold)
                    ExpenseText(text = "Welcome back", fontSize = 24.sp,color = Color.White, fontWeight = FontWeight.Bold)
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_notification),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
            val state by viewModel.expenses.observeAsState(initial = emptyList())
            val balance = viewModel.getBalance(state)
            val expenses = viewModel.getTotalExpense(state)
            val income = viewModel.getTotalIncome(state)
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
                list = state,
                sharedViewModel = sharedViewModel,
                navController = navController
            )
            Box(
                modifier = Modifier
                    .padding(top = 8.dp , start = 8.dp,end = 45.dp, bottom = 45.dp)
                    .size(60.dp)
                    .constrainAs(add){
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = Zinc)
                    .clickable {
                        navController.navigate(ScreenRoute.AddExpenseScreenRoute.route)
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "small floating action button",
                    modifier = Modifier.size(45.dp)
                )
            }
        }
    }
}



@Composable
fun TransactionList(modifier: Modifier, list : List<ExpenseEntity>, sharedViewModel: SharedViewModel,navController: NavController, title: String = "Recent Transactions") {
    LazyColumn(modifier = modifier.padding(horizontal = 16.dp)) {
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                ExpenseText(text = title, fontSize = 20.sp)
                if(title == "Recent Transactions") {
                    ExpenseText(
                        text = "See All",
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
        }
        items(list){item ->
            TransactionItem(
                title = item.title,
                amount = item.amount.toString(),
                icon = Utils.getItemIcon(item),
                date = item.date,
                color = if(item.type == "Income") Color.Green else Color.Red,
                Modifier.clickable { sharedViewModel.selectedExpense(item)
                    navController.navigate(ScreenRoute.UpdateScreenRoute.route) }
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
    modifier: Modifier,
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
    HomeScreen(rememberNavController(), SharedViewModel() )
}