var navbar = `

  <nav class="navbar navbar-expand-lg navbar-light">
      <div class="container">
        <a class="navbar-brand" href="LandingPage.html"><img src="images/cropped-favicon.webp" alt="Logo" class="img-fluid" /></a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown"
        aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNavDropdown">
        <ul class="navbar-nav mx-lg-auto">
            <li class="nav-item">
            <a class="nav-link active" href="ScheduleView.html"> View Schedule </a>
            </li>
            <li class="nav-item">
            <a class="nav-link active" href="AboutPage.html"> About </a>
            </li>
            <li class="nav-item">
            <a class="nav-link active" href="Contact.html">Contact Us</a>
            </li>
        </ul>
        <a class="btn btn-crimson ms-3" href="Login.html"> Login </a>
        </div>
    </div>
  </nav>`

document.body.insertAdjacentHTML("afterbegin", navbar);
