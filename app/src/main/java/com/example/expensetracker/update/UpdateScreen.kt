package com.example.expensetracker.update

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.expensetracker.R
import com.example.expensetracker.data.ExpenseEntity
import com.example.expensetracker.widget.ExpenseText
import com.example.expensetracker.widget.Utils
import com.example.expensetracker.widget.Utils.getMillisFromDate
import kotlinx.coroutines.launch

@Composable
fun UpdateScreen(navController: NavController,expenseEntity: ExpenseEntity){
    val viewModel: UpdateExpenseViewModel = UpdateExpenseViewModelFactory(LocalContext.current).create(
        UpdateExpenseViewModel::class.java)
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
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable{
                            navController.popBackStack()
                        }
                )
                ExpenseText(text ="Update Expense",
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
                }, expenseEntity,
                onUpdateExpenseClicked = {
                    coroutineScope.launch {
                        if (viewModel.updateExpense(it)) {
                            navController.popBackStack()
                        }
                    }
                },
                onDeleteExpenseClicked = {
                    coroutineScope.launch {
                        if(viewModel.deleteExpense(it)){
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun DataForm(modifier: Modifier, expenseEntity: ExpenseEntity ,onUpdateExpenseClicked:(model: ExpenseEntity)->Unit,onDeleteExpenseClicked:(model: ExpenseEntity)->Unit){
    val name = remember{ mutableStateOf(expenseEntity.title) }
    val amount = remember{ mutableStateOf(expenseEntity.amount.toString()) }
    val date = remember{ mutableLongStateOf(getMillisFromDate(expenseEntity.date)) }
    val dateDialogVisibility = remember { mutableStateOf(false) }
    val category = remember{ mutableStateOf(expenseEntity.category) }
    val type = remember{ mutableStateOf(expenseEntity.type) }

    Column(modifier = modifier
        .padding(16.dp)
        .fillMaxWidth()
        .shadow(16.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Color.White)
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
    ) {
        ExpenseText(text = "Name", fontSize = 16.sp)
        OutlinedTextField(value = name.value,
            onValueChange = { name.value = it },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = { ExpenseText(text = "Enter Expense Name") }
        )

        Spacer(modifier = Modifier.size(8.dp))
        ExpenseText(text = "Amount", fontSize = 16.sp)
        OutlinedTextField(value = amount.value,
            onValueChange = { amount.value = it },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = { ExpenseText(text = "Enter Amount") }
        )

        Spacer(modifier = Modifier.size(8.dp))
        ExpenseText(text = "Date", fontSize = 16.sp)
        OutlinedTextField(value = if (date.longValue == 0L) "" else Utils.formatDateToReadableForm(
            date.longValue
        ),
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .clickable { dateDialogVisibility.value = true },
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = Color.Black, disabledTextColor = Color.Black,
                disabledPlaceholderColor = Color.Black,
            ),
            placeholder = { ExpenseText(text = "Select Date") }
        )

        Spacer(modifier = Modifier.size(8.dp))
        ExpenseText(text = "Type", fontSize = 16.sp)
        Spacer(modifier = Modifier.size(4.dp))
        ExpenseDropDown(list = listOf("Income", "Expense")) {
            type.value = it
        }

        Spacer(modifier = Modifier.size(8.dp))
        ExpenseText(text = "Category", fontSize = 16.sp)
        Spacer(modifier = Modifier.size(4.dp))
        ExpenseDropDown(
            list = listOf(
                "Salary",
                "Netflix",
                "Paypal",
                "Starbucks",
                "Youtube",
                "Upwork",
                "Other"
            )
        ) {
            category.value = it
        }

        Spacer(modifier = Modifier.size(16.dp))
        Row (horizontalArrangement = Arrangement.SpaceBetween){
            Button(
                onClick = {
                    val model = ExpenseEntity(
                        expenseEntity.id,
                        name.value,
                        amount.value.toDoubleOrNull() ?: 0.0,
                        Utils.formatDateToReadableForm(date.value),
                        category.value,
                        type.value
                    )
                    onUpdateExpenseClicked(model)
                }, modifier = Modifier.weight(1f)
            ) {
                ExpenseText(text = "Update", fontSize = 16.sp,maxLines = 1)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    onDeleteExpenseClicked(expenseEntity)
                },modifier = Modifier.weight(1f)
            ) {
                ExpenseText(text = "Delete", fontSize = 16.sp,maxLines = 1)
            }
            if (dateDialogVisibility.value) {
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
        }
        }
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
                DropdownMenuItem(text = { ExpenseText(text = it) },
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
