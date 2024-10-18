package com.example.expensetracker.addExpense

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.expensetracker.R
import com.example.expensetracker.Utils
import com.example.expensetracker.data.model.ExpenseEntity
import com.example.expensetracker.home.HomeScreenViewModel
import com.example.expensetracker.home.HomeViewModelFactory
import com.example.expensetracker.widget.ExpenseText
import kotlinx.coroutines.launch

@Composable
fun AddExpenseScreen(){
    val viewModel: AddExpenseViewModel = AddExpenseViewModelFactory(LocalContext.current).create(AddExpenseViewModel::class.java)
    val coroutineScope = rememberCoroutineScope()
    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow,card,topBar) = createRefs()
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
                },
                onAddExpenseClicked = {
                    coroutineScope.launch {
                        viewModel.addExpense(it)
                    }
                }
            )
        }
    }
}

@Composable
fun DataForm(modifier:Modifier, onAddExpenseClicked:(model:ExpenseEntity)->Unit){
    val name = remember{ mutableStateOf("") }
    val amount = remember{ mutableStateOf("") }
    val date = remember{ mutableLongStateOf(0L) }
    val dateDialogVisibility = remember { mutableStateOf(false) }
    val category = remember{ mutableStateOf("") }
    val type = remember{ mutableStateOf("") }

    Column(modifier = modifier
        .padding(16.dp)
        .fillMaxWidth()
        .shadow(16.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Color.White)
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
    ){
        ExpenseText(text = "Name", fontSize = 16.sp)
        OutlinedTextField(value = name.value, onValueChange ={ name.value = it}, modifier = Modifier
            .fillMaxWidth(),
            label ={ ExpenseText("Enter Expense Name") }
        )

        Spacer(modifier = Modifier.size(8.dp))
        ExpenseText(text = "Amount", fontSize = 16.sp)
        OutlinedTextField(value = amount.value, onValueChange = {amount.value = it}, modifier = Modifier
            .fillMaxWidth(),
            label ={ ExpenseText("Enter Amount")}
        )

        Spacer(modifier = Modifier.size(8.dp))
        ExpenseText(text = "Date", fontSize = 16.sp)
        OutlinedTextField(value = if(date.value == 0L) "" else Utils.formatDateToReadableForm(date.value), onValueChange = {}, modifier = Modifier
            .fillMaxWidth()
            .clickable { dateDialogVisibility.value = true },
            label = { ExpenseText("Select Date")}
        )

        Spacer(modifier = Modifier.size(8.dp))
        ExpenseText(text = "Type", fontSize = 16.sp)
        Spacer(modifier = Modifier.size(4.dp))
        ExpenseDropDown(list = listOf("Income","Expense")) {
            type.value = it
        }

        Spacer(modifier = Modifier.size(8.dp))
        ExpenseText(text = "Category", fontSize = 16.sp)
        Spacer(modifier = Modifier.size(4.dp))
        ExpenseDropDown(list = listOf("Salary","Netflix", "Paypal", "Starbucks", "Youtube", "Upwork","Other")) {
           category.value = it
        }

        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = {
                val model = ExpenseEntity(null,
                    name.value,
                    amount.value.toDoubleOrNull()?: 0.0,
                    Utils.formatDateToReadableForm(date.value),
                    category.value,
                    type.value
                )
            onAddExpenseClicked(model)
            },
            modifier = Modifier.fillMaxWidth()) {
            ExpenseText(text = "Add Expense", fontSize = 16.sp)
        }
        if(dateDialogVisibility.value) {
            ExpenseDatePickerDialog(
                onDateSelect = {
                    date.value = it
                    dateDialogVisibility.value = false
                },
                onDismiss = {
                    dateDialogVisibility.value = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDatePickerDialog(
    onDateSelect :(date:Long) -> Unit,
    onDismiss:()-> Unit
){
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis ?: 0L
    DatePickerDialog(onDismissRequest = { onDismiss() },
        confirmButton = { TextButton(onClick = { onDateSelect(selectedDate) }) {
            ExpenseText(text = "Confirm")
        } },
        dismissButton = { TextButton(onClick = { onDismiss()}) {
            ExpenseText(text = "Cancel")
        }}
        ) {
            DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDropDown(list : List<String> , onItemSelected:(item:String)-> Unit){
    val expanded = remember{ mutableStateOf(false) }
    val selectedItem = remember{ mutableStateOf(list[0]) }

    ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = {expanded.value = it}) {
        TextField(value = selectedItem.value, onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
            }
        )
        ExposedDropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false}) {
            list.forEach{
                DropdownMenuItem(text = { ExpenseText(text = it)},
                    onClick = {
                        selectedItem.value = it
                        onItemSelected(selectedItem.value)
                        expanded.value = false
                    }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AddExpensePreview(){
    AddExpenseScreen()
}
