package com.javix.wordflipster

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(AndroidJUnit4::class)
class HomeViewModelTest {

//    @get:Rule
//    val instantExecutorRule = InstantTaskExecutorRule()



    private lateinit var viewModel: HomeViewModel
     val testDispatcher = StandardTestDispatcher()


//    val context: Context = ApplicationProvider.getApplicationContext()

//    private lateinit var dataStoreManager: DataStoreManager

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        // Mock dataStoreManager with sample flows

//        dataStoreManager = mock(TestDataStoreManager::class.java)
//        `when`(dataStoreManager.minuteCountFlow).thenReturn(MutableStateFlow(1))
//        `when`(dataStoreManager.letterCountFlow).thenReturn(MutableStateFlow(2))
//        `when`(dataStoreManager.vibrationEnabledFlow).thenReturn(MutableStateFlow(true))
//        `when`(dataStoreManager.totalWords).thenReturn(MutableStateFlow(5))
//        `when`(dataStoreManager.totalCorrectWords).thenReturn(MutableStateFlow(3))

        viewModel = HomeViewModel(
            ApplicationProvider.getApplicationContext(),
            category = "Animals",
            onChallengeCompleteListener = {}
        )
    }

    @Test
    fun initial_values_are_loaded_correctly_from_dataStoreManager() = runTest {
        advanceUntilIdle() // Ensure all coroutines finish
        assertEquals(60, viewModel.remainingTime.value)
        assertEquals(2, viewModel.letterCount.value)
        assertEquals(5, viewModel.totalWords.value)
        assertTrue(viewModel.vibration.value)
    }
//
//    @Test
//    fun `countdown timer reduces remaining time`() = runTest {
//        advanceUntilIdle() // Start timer
//        assertTrue(viewModel.remainingTime.value > 0)
//        advanceTimeBy(1000) // Fast-forward 1 second
//        assertEquals(59, viewModel.remainingTime.value)
//    }
//
//    @Test
//    fun `challenge is created and saved when timer ends`() = runTest {
//        viewModel._remainingTime.value = 1 // Set timer to almost end
//        advanceTimeBy(1000) // Fast-forward 1 second
//
//        // Check if challenge is saved
//        verify(challengeDao).insertChallenge(any())
//        assertNotNull(viewModel.createChallenge())
//    }
//
//    @Test
//    fun `getWordsListFromCount returns correct word list`() = runTest {
//        val wordsList = viewModel.getWordsListFromCount(3)
//        assertEquals(threeLetterWords, wordsList) // Assuming 'threeLetterWords' is defined
//    }
//
//    @Test
//    fun `updateCorrectWords updates wordsSolved state`() = runTest {
//        viewModel.updateCorrectWords(10)
//        assertEquals(10, viewModel.wordsSolved.value)
//    }
//
//    @Test
//    fun `updateTotalWords updates totalWords state`() = runTest {
//        viewModel.updateTotalWords(20)
//        assertEquals(20, viewModel.totalWords.value)
//    }

    // Other tests could include verifying the correct list by category, checking vibration setting, etc.

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
class TestDataStoreManager : DataStoreManager(ApplicationProvider.getApplicationContext()) {
    override val minuteCountFlow = MutableStateFlow(1)
    override val letterCountFlow = MutableStateFlow(2)
    override val vibrationEnabledFlow = MutableStateFlow(true)
    override val totalWords = MutableStateFlow(5)
    override val totalCorrectWords = MutableStateFlow(3)
}