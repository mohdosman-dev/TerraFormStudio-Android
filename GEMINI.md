# Terra Form Studio - Android App Guidelines

## Project Vision
A premium, "Tactile Gallery" mobile experience for handmade ceramics. The app prioritizes artisan storytelling and high-fidelity visual discovery.

## Tech Stack & Architecture Mandates
- **Language:** Kotlin
- **UI:** Jetpack Compose (Modern, declarative UI)
- **Architecture:** MVI (Model-View-Intent) 
  - *Reference:* Activate `android-presentation-mvi` for state/action/event patterns.
- **Networking:** Retrofit
- **Image Loading:** Coil
- **DI:** Koin
  - *Reference:* Activate `android-di-koin` for module setup.
- **Structure:** Single module (`:app`) for initial development.

## Resource Map (Lazy Loading)
To minimize context bloat, refer to these resources only when relevant to the current task:

- **UI Mockups:** `specs/screen.html` (Primary visual reference for layout/spacing).
- **Functional Requirements:** `specs/terra-design.md` (Source of truth for logic and behavior).
- **Backend API:** Discover routes and schemas on-the-fly by reading `workspace/test/terra-form-studio/backend/src/routes/`.
- **Aesthetic Guidelines:** Follow "The Tactile Gallery" theme (Noto Serif, earth tones, premium whitespace).

## Feature Map

| Feature | Route | ViewModel | Screen |
|---------|-------|-----------|--------|
| Home | `HomeRoute` | `HomeViewModel` | `HomeScreen` |
| Product Detail | `ProductDetailRoute(slug)` | `ProductDetailViewModel` | `ProductDetailScreen` |
| Artisan Profile | `ArtisanProfileRoute(slug)` | `ArtisanProfileViewModel` | `ArtisanProfileScreen` |

## Context Commands
> [!IMPORTANT]
> **UI Work:** Always read `specs/terra-design.md` and `specs/screen.html` before implementing or modifying screens.
> **Networking:** Always check `backend/` route definitions before creating Retrofit interfaces to ensure contract alignment.

## Engineering Standards
- **Surgical Updates:** Research -> Strategy -> Execution.
- **Git Workflow:**
  - **Auto-Stage:** Every file created or modified must be added to git automatically.
  - **Feature Branches:** Create a new branch for each feature (e.g., `feature/home-screen`).
  - **Modular Commits:** Create meaningful, atomic commits that clearly explain the *why* behind changes.
  - **No Auto-Merge:** Do NOT merge feature branches into `main` until explicitly asked.
- **MVI Purity:** Keep ViewModels focused on state reduction. Use `android-error-handling` for Result wrappers.
- **Testing:** 
  - Write unit tests for ViewModels and Repositories.
  - Write Compose UI tests for critical flows.
  - **Mandate:** DO NOT run tests automatically. Only execute when explicitly requested.
- **Code Style:** Follow `android-compose-ui` best practices for stability and performance.

## Design Tokens (Quick Ref)
- **Primary Font:** Noto Serif
- **Background:** #F5F1EB (Soft Gallery Canvas)
- **Primary CTA:** #8D775F
- **Typography:** Emphasis on hierarchy and tactile readability.
