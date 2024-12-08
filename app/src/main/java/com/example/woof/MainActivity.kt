package com.example.woof

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.woof.data.Dog
import com.example.woof.data.dogs
import com.example.woof.ui.theme.WoofTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WoofTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    WoofApp()
                }
            }
        }
    }
}

@Composable
fun WoofApp() {
    // استخدام MutableState للتحكم في الوضع (داكن أو فاتح)
    var darkMode by remember { mutableStateOf(false) } // الفاتح بشكل افتراضي
    var selectedDog by remember { mutableStateOf<Dog?>(null) } // لحفظ الكلب المختار

    Scaffold(
        topBar = {
            WoofTopAppBar()
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            // زر أو تبديل لاختيار الوضع الفاتح أو الداكن
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Dark Mode", style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = darkMode,
                    onCheckedChange = { darkMode = it }
                )
            }

            // تطبيق الثيم بناءً على وضع الاختيار
            WoofTheme(darkTheme = darkMode) {
                LazyColumn(contentPadding = paddingValues) {
                    items(dogs) { dog ->
                        DogItem(
                            dog = dog,
                            modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)),
                            onDogClick = {
                                selectedDog = if (selectedDog == dog) null else dog
                            }
                        )

                        // عرض تفاصيل الكلب أسفل الكلب إذا تم اختياره
                        selectedDog?.let {
                            if (it == dog) {
                                DogDetails(dog) // عرض تفاصيل الكلب أسفل الكلب
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DogItem(
    dog: Dog,
    modifier: Modifier = Modifier,
    onDogClick: (Dog) -> Unit // دالة للضغط على الكلب
) {
    Card(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_small))
                .clickable { onDogClick(dog) } // عند الضغط على الكلب، تنفيذ الدالة
        ) {
            DogIcon(dog.imageResourceId)
            DogInformation(dog.name, dog.age)
        }
    }
}

@Composable
fun WoofTopAppBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(dimensionResource(R.dimen.image_size))
                        .padding(dimensionResource(R.dimen.padding_small)),
                    painter = painterResource(R.drawable.ic_woof_logo),
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.displayLarge
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun DogIcon(
    @DrawableRes dogIcon: Int,
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier
            .size(dimensionResource(R.dimen.image_size))
            .padding(dimensionResource(R.dimen.padding_small))
            .clip(MaterialTheme.shapes.small),
        contentScale = ContentScale.Crop,
        painter = painterResource(dogIcon),
        contentDescription = null
    )
}

@Composable
fun DogInformation(
    @StringRes dogName: Int,
    dogAge: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(dogName),
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
        )
        Text(
            text = stringResource(R.string.years_old, dogAge),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun DogDetails(dog: Dog) {
    Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))) {
        Text(
            text = stringResource(dog.name),
            style = MaterialTheme.typography.displayMedium
        )
        Text(
            text = stringResource(R.string.years_old, dog.age),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = stringResource(dog.hobbies),
            style = MaterialTheme.typography.bodyLarge
        )
        DogIcon(dog.imageResourceId) // عرض صورة الكلب
    }
}

@Preview
@Composable
fun WoofPreview() {
    WoofTheme(darkTheme = false) {
        WoofApp()
    }
}

@Preview
@Composable
fun WoofDarkThemePreview() {
    WoofTheme(darkTheme = true) {
        WoofApp()
    }
}
