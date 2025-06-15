<!-- Use this file to provide workspace-specific custom instructions to Copilot. For more details, visit https://code.visualstudio.com/docs/copilot/copilot-customization#_use-a-githubcopilotinstructionsmd-file -->

# DereGuide Android Development Instructions

This is an Android application for THE iDOLM@STER Cinderella Girls Starlight Stage, written in Kotlin using modern Android development practices.

## Project Architecture

- **Architecture Pattern**: MVVM (Model-View-ViewModel)
- **UI Framework**: Jetpack Compose with Material Design 3
- **Database**: Room with SQLite
- **Networking**: Retrofit with OkHttp
- **Dependency Injection**: Hilt (Dagger)
- **Asynchronous Programming**: Kotlin Coroutines and Flow

## Key Components

### Data Layer
- `data/model/`: Contains data classes for Cards, Characters, Songs, Events, Teams, and Gacha
- `data/database/`: Room database with DAOs and converters
- `data/api/`: Retrofit API service interfaces
- `data/repository/`: Repository pattern for data management

### UI Layer
- `ui/screens/`: Composable screens for different features
- `ui/components/`: Reusable UI components
- `ui/viewmodel/`: ViewModels for managing UI state
- `ui/theme/`: Material Design 3 theming

### Core Features
- **Card Viewer**: Browse and filter idol cards by attributes, rarity, and more
- **Song/Beatmap Viewer**: Display song information and beatmaps
- **Character Profiles**: View idol information with birthday reminders
- **Team Editor**: Create and manage teams for live performances
- **Live Score Simulator**: Calculate live performance scores
- **Gacha Simulator**: Simulate card draws from gacha pools
- **Event Viewer**: Display current and past event information

## Development Guidelines

### Code Style
- Follow Kotlin official coding conventions
- Use meaningful variable and function names
- Prefer Compose over View-based UI
- Use StateFlow for UI state management
- Implement proper error handling with Result types

### Architecture Guidelines
- Keep ViewModels free of Android dependencies
- Use Repository pattern for data access
- Implement offline-first approach with Room database
- Use Hilt for dependency injection
- Follow single responsibility principle

### UI Guidelines
- Use Material Design 3 components
- Implement proper loading and error states
- Support dark/light theme
- Ensure accessibility compliance
- Use consistent spacing and typography

### Testing
- Write unit tests for ViewModels and Repositories
- Use MockK for mocking in tests
- Test database operations with Room testing utilities
- Implement UI tests for critical user flows

## Common Patterns

### Data Fetching
```kotlin
// Repository pattern with offline-first approach
suspend fun getCards(): Flow<List<Card>> = flow {
    emit(localDataSource.getCards())
    try {
        val remoteCards = remoteDataSource.getCards()
        localDataSource.saveCards(remoteCards)
        emit(localDataSource.getCards())
    } catch (e: Exception) {
        // Handle error, emit cached data
    }
}
```

### State Management
```kotlin
// ViewModel with UiState pattern
data class UiState(
    val isLoading: Boolean = false,
    val data: List<Item> = emptyList(),
    val error: String? = null
)

class MyViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
}
```

### Error Handling
- Use Result<T> for operations that can fail
- Provide meaningful error messages to users
- Implement retry mechanisms for network operations
- Log errors appropriately for debugging

## Resource Guidelines
- Use string resources for all user-facing text
- Support multiple languages where applicable
- Use vector drawables for icons
- Follow Material Design color guidelines
- Implement proper content descriptions for accessibility
