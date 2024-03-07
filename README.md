# TobaMembers
This Android application is developed as Simple Application, showcasing a login/signup system and differentiated functionalities for users based on their roles within the app.

https://github.com/girendi/TobaMembers/assets/24579720/673c547f-02ee-4a1e-9ae2-278b4e67b743

## Features
<ul>
  <li>Login and Sign Up: Secure authentication system for users to access their respective dashboards.</li>
  <li>User Role Differentiation: The app distinguishes between Normal Users and Admins, providing each with unique functionalities.
    <ul>
      <li>Normal User: Can view a list of items with the ability to sort and access them in an infinite scroll view.</li>
      <li>Admin: Has the capability to view, update, and delete registered users, including a secure delete function with verification.</li>
    </ul>
  </li>
</ul>

## Getting Started
### Prerequisites
<ul>
  <li>Android Studio</li>
</ul>

### Setup
<ol>
  <li>Clone this repository and import into Android Studio
    <code>https://github.com/girendi/TobaMembers.git</code>.
  </li>
  <li>Build the project in Android Studio.</li>
  <li>Run the application on an emulator or physical device.</li>
</ol>

### Built With
<ul>
  <li>[Kotlin] - Primary programming language.</li>
  <li>[Retrofit] - For network requests.</li>
  <li>[Room] - For local data storage.</li>
  <li>[Clean Architecture] - This project is structured using Clean Architecture principles to ensure separation of concerns, scalability, and easier maintenance.</li>
  <li>[Coroutine Flow] - Utilized Coroutine Flow for managing asynchronous data streams with lifecycle-aware components, enhancing the app's responsiveness and performance.</li>
  <li>[Koin] - A lightweight dependency injection framework, Koin is used for managing component dependencies, making the code more modular and testable.</li>
</ul>

### Authors
Gideon Panjaitan - *Initial Work* - [girendi](https://github.com/girendi)
