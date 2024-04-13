const apiUrls = {
  user: 'http://localhost:8080/user',
  book: 'http://localhost:8080/book'
};

document.addEventListener('DOMContentLoaded', () => {
  submitEventListener('registerForm', registerUser);
  submitEventListener('loginForm', loginUser);
  clickEventListener('logoutNavItem', logoutUser);
  submitEventListener('add-book-form', addBook);
  submitEventListener('borrow-book-form', borrowBook);
  submitEventListener('remove-book-form', removeBook);
  submitEventListener('return-book-form', returnBook);

  initializeUserDetails();
  getAllBooks();
});

function submitEventListener(formId, event) {
  const form = document.getElementById(formId);
  if (form) {
    form.addEventListener('submit', event);
  }
}

function clickEventListener(elementId, handler) {
  const element = document.getElementById(elementId);
  if (element) {
    element.addEventListener('click', handler);
  }
}

async function makeApiCallWith(url, method, requestData) {
  try {
    const response = await fetch(url, {
      method: method,
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(requestData)
    });

    return await response.json();
  } catch (error) {
    console.error(`Error making API call to ${url}:`, error);
    throw new Error(`Failed to make API call to ${url}`);
  }
}

async function registerUser(event) {
  event.preventDefault();

  const formData = new FormData(this);
  const requestData = {
    username: formData.get('username'),
    password: formData.get('password'),
    role: formData.get('role')
  };

  try {
    const response = await makeApiCallWith(`${apiUrls.user}/register`, 'POST', requestData);

    if (response.successful) {
      handleRegistrationSuccess(response.data);
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
  redirectToUsersPage(userData);
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
    const response = await makeApiCallWith(`${apiUrls.user}/login`, 'PATCH', requestData);

    if (response.successful) {
      handleLoginSuccess(response.data);
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
  redirectToUsersPage(userData);
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
    const response = await makeApiCallWith(`${apiUrls.user}/logout`, 'PATCH', requestData);

    if (response.successful) {
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
  displayDetailsIn(userData);

  const usernameElement = document.getElementById('user-name');
  const username = usernameElement.textContent.trim();
  sessionStorage.setItem('username', username);
}

function redirectToUsersPage(userData) {
  const queryString = Object.keys(userData).map(key => key + '=' + userData[key]).join('&');
  window.location.href = `users.html?${queryString}`;
}

function displayDetailsIn(userData) {
  const userIdElement = document.getElementById('user-id');
  userIdElement.innerHTML = `<strong>ID:</strong> ${userData.id}`;
  userIdElement.style.color = '#555';

  const userNameElement = document.getElementById('user-name');
  userNameElement.innerHTML = `<strong>Username:</strong> ${userData.username}`;
  userNameElement.style.color = '#555';
}

async function getAllBooks() {
  try {
    const response = await makeApiCallWith(`${apiUrls.book}/all`, 'GET');

    if (!response.successful) {
      throw new Error(`Error: ${response.status}`);
    }

    const books = response.data.books;
    displayBooks(books);
  } catch (error) {
    console.error('Failed to fetch books:', error);
  }
}

function displayBooks(books) {
  const bookList = document.getElementById('bookList');
  bookList.innerHTML = '';

  books.forEach(book => {
    const bookItem = document.createElement('div');
    bookItem.classList.add('book-item');
    bookItem.innerHTML = `
      <p class="title">Title: ${book.title}</p>
      <p class="author">Author: ${book.author}</p>
      <p class="genre">Genre: ${book.genre}</p>
      <p class="genre">ID: ${book.id}</p>`;
    bookList.appendChild(bookItem);
  });
}

async function addBook(event) {
  event.preventDefault();

  const username = sessionStorage.getItem('username').replace('Username: ', '');
  if (!username) {
    console.error('Username not found in session storage');
    alert('Username not found. Please try logging in again.');
    return;
  }

  const formData = new FormData(this);
  const requestData = {
    username: username,
    title: formData.get('title'),
    author: formData.get('author'),
    genre: formData.get('genre'),
    quantity: formData.get("quantity")
  };

  try {
    const response = await makeApiCallWith(`${apiUrls.user}/add-book`, 'POST', requestData);

    if (response.successful) {
      alert('Book added successfully.');
      display(response);
    } else {
      alert('Failed to add book. Please try again.');
      display(response);
    }
  } catch (error) {
    console.error('Error adding book:', error);
    alert('Failed to add book. Please try again.');
  }
}

async function borrowBook(event) {
  event.preventDefault();

  const username = sessionStorage.getItem('username').replace('Username: ', '');
  if (!username) {
    console.error('Username not found in session storage');
    alert('Username not found. Please try logging in again.');
    return;
  }

  const formData = new FormData(this);
  const requestData = {
    username: username,
    bookId: formData.get('borrow-book-id')
  };

  try {
    const response = await makeApiCallWith(`${apiUrls.user}/borrow-book`, 'PATCH', requestData);

    if (response.successful) {
      alert('Book borrowed successfully.');
      display(response);
    } else {
      alert('Failed to borrow book. Please try again.');
      display(response);
    }
  } catch (error) {
    console.error('Error borrowing book:', error);
    alert('Failed to borrow book. Please try again.');
  }
}

async function removeBook(event) {
  event.preventDefault();

  const username = sessionStorage.getItem('username').replace('Username: ', '');
  if (!username) {
    console.error('Username not found in session storage');
    alert('Username not found. Please try logging in again.');
    return;
  }

  const formData = new FormData(this);
  const requestData = {
    username: username,
    bookId: formData.get("remove-book-id")
  };
  try {
    const response = await makeApiCallWith(`${apiUrls.user}/remove-book`, 'DELETE', requestData);

    if (response.successful) {
      alert('Book removed successfully.');
      display(response);
    } else {
      alert('Failed to remove book. Please try again.');
      display(response);
    }
  } catch (error) {
    console.error('Error removing book:', error);
    alert('Failed to remove book. Please try again.');
  }
}

async function returnBook(event) {
  event.preventDefault();

  const username = sessionStorage.getItem('username').replace('Username: ', '');
  if (!username) {
    console.error('Username not found in session storage');
    alert('Username not found. Please try logging in again.');
    return;
  }

  const formData = new FormData(this);
  const requestData = {
    username: username,
    libraryLoanId: formData.get("library-loan-id")
  };
  try {
    const response = await makeApiCallWith(`${apiUrls.user}/return-book`, 'PATCH', requestData);

    if (response.successful) {
      alert('Book returned successfully.');
      display(response);
    } else {
      alert('Failed to return book. Please try again.');
      display(response);
    }
  } catch (error) {
    console.error('Error returning book:', error);
    alert('Failed to return book. Please try again.');
  }
}

function display(response) {
  const responseData = document.getElementById('response-data');
  if (responseData) {
    responseData.innerHTML = `
      ${response.successful ? `<p class="successful">Successful: ${response.successful}</p>` : ''}
      ${!response.successful ? `<p class="error">Error: ${response.successful}</p>` : ''}
      <p class="response">Response: ${JSON.stringify(response.data)}</p>`;
  }
}
