const userApiUrl = 'http://localhost:8080/user';
const bookApiUrl = 'http://localhost:8080/book';

document.addEventListener('DOMContentLoaded', () => {
  if (document.getElementById('registerForm')) {
    document.getElementById('registerForm').addEventListener('submit', registerUser);
  }

  if (document.getElementById('loginForm')) {
    document.getElementById('loginForm').addEventListener('submit', loginUser);
  }

  if (document.getElementById('logoutNavItem')) {
    document.getElementById('logoutNavItem').addEventListener('click', logoutUser);
  }


  initializeUserDetails();
  fetchAllBooks();

});

async function registerUser(event) {
  event.preventDefault();

  const formData = new FormData(this);
  const requestData = {
    username: formData.get('username'),
    password: formData.get('password'),
    role: formData.get('role')
  };

  try {
    const response = await fetch(`${userApiUrl}/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(requestData)
    });

    const responseData = await response.json();
    if (responseData.successful) {
      handleRegistrationSuccess(responseData.data);
    } else {
      handleRegistrationFailure();
    }
  } catch (error) {
    console.error('Registration failed:', error);
    alert('Registration failed. Please try again.');
  }
}

function handleRegistrationSuccess(userData) {
  alert('Registration successful.');
  redirectToRegisteredUserPage(userData);
}

function handleRegistrationFailure() {
  alert('Registration failed. Please try again.');
}

async function loginUser(event) {
  event.preventDefault();

  const formData = new FormData(this);
  const requestData = {
    username: formData.get('username'),
    password: formData.get('password')
  };

  try {
    const response = await fetch(`${userApiUrl}/login`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(requestData)
    });

    const responseData = await response.json();
    if (responseData.successful) {
      handleLoginSuccess(responseData.data);
    } else {
      handleLoginFailure();
    }
  } catch (error) {
    console.error('Login failed:', error);
    alert('Login failed. Please try again.');
  }
}

function handleLoginSuccess(userData) {
  alert('Login successful.');
  redirectToRegisteredUserPage(userData);
}

function handleLoginFailure() {
  alert('Login failed. Please check your credentials and try again.');
}

async function logoutUser(event) {
  event.preventDefault();

  const username = sessionStorage.getItem('username').replace('Username: ', '');
  if (!username) {
    console.error('Username not found in session storage');
    alert('Username not found. Please try logging in again.');
    return;
  }

  const requestData = {
    username: username
  };

  try {
    const response = await fetch(`${userApiUrl}/logout`, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(requestData)
    });

    const responseData = await response.json();
    if (responseData.successful) {
      handleLogoutSuccess();
    } else {
      handleLogoutFailure();
    }
  } catch (error) {
    console.error('Logout failed:', error);
    alert('Logout failed. Please try again.');
  }
}

function handleLogoutSuccess() {
  alert('Logout successful.');
  window.location.href = 'login-page.html';

}

function handleLogoutFailure() {
  alert('Logout failed. Please try again.');
}

async function initializeUserDetails() {
  const urlParams = new URLSearchParams(window.location.search);
  const userData = {};
  for (const [key, value] of urlParams) {
    userData[key] = value;
  }
  displayUserDetails(userData);

  const usernameElement = document.getElementById('user-name');
  const username = usernameElement.textContent.trim();
  sessionStorage.setItem('username', username);
}

function redirectToRegisteredUserPage(userData) {
  const queryString = Object.keys(userData).map(key => key + '=' + userData[key]).join('&');
  window.location.href = `users.html?${queryString}`;
}

function displayUserDetails(userData) {
  const userIdElement = document.getElementById('user-id');
  userIdElement.innerHTML = `<strong>ID:</strong> ${userData.id}`;
  userIdElement.style.color = '#555';

  const userNameElement = document.getElementById('user-name');
  userNameElement.innerHTML = `<strong>Username:</strong> ${userData.username}`;
  userNameElement.style.color = '#555';
}

async function fetchAllBooks() {
  try {
    const response = await fetch(`${bookApiUrl}/all`);

    if (!response.ok) {
      throw new Error(`Error: ${response.status}`);
    }

    const responseData = await response.json();
    if (responseData.successful) {
      const books = responseData.data.books;
      displayBooks(books);
    } else {
      console.error('Failed to fetch books:', responseData.message);
    }
  } catch (error) {
    console.error('Failed to fetch books:', error);
  }
}

function displayBooks(books) {
  const bookListContainer = document.getElementById('bookList');
  bookListContainer.innerHTML = '';

  books.forEach(book => {
    const bookItem = document.createElement('div');
    bookItem.classList.add('book-item');
    bookItem.innerHTML = `
      <p class="title">Title: ${book.title}</p>
      <p class="author">Author: ${book.author}</p>
      <p class="genre">Genre: ${book.genre}</p>`;
    bookListContainer.appendChild(bookItem);
  });
}

