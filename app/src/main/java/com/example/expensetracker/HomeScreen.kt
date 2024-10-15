package com.example.expensetracker

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.expensetracker.ui.theme.Zinc

@Composable
fun HomeScreen(){
    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameBox,list,card,topBar) = createRefs()
            Image(painter = painterResource(id = R.drawable.ic_topbar), contentDescription = null,
                modifier = Modifier.constrainAs(topBar){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp, start = 16.dp, end = 16.dp)
                .constrainAs(nameBox) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }){
                Column {
                    Text(text = "Hello," , fontSize = 16.sp, color = Color.White)
                    Text(text = "Abhinn Agrawal", fontSize = 20.sp, fontWeight = FontWeight.Bold,color = Color.White)
                }
                Image(painter = painterResource(id = R.drawable.ic_notification),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
            CardItem(modifier = Modifier.constrainAs(card){
                top.linkTo(nameBox.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
        }
    }
}

@Composable
fun CardItem(modifier: Modifier){
    Column(modifier = modifier
        .padding(16.dp)
        .fillMaxWidth()
        .height(200.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Zinc)
        .padding(16.dp)
    ){
        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)){
            Column(modifier = Modifier.align(Alignment.CenterStart)){
                Text(text = "Total Balance", fontSize = 16.sp, color = Color.White)
                Text(text = "INR 50,000", fontSize = 20.sp, fontWeight = FontWeight.Bold,color = Color.White)
            }
            Image(painter = painterResource(id = R.drawable.dots_menu),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
            verticalAlignment = Alignment.CenterVertically){
            Column {
                Row{
                    Image(painter = painterResource(id = R.drawable.ic_income), contentDescription = null)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text ="Income", fontSize = 16.sp, color = Color.White )
                }
                Text(text = "INR 10,000", fontSize = 20.sp, color = Color.White)
            }
        }
    }
}

@Composable
@Preview (showBackground = true)
fun previewHomeScreen(){
    HomeScreen()
}