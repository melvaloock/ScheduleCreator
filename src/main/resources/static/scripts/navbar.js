var navbar = `

  <nav class="navbar navbar-expand-lg navbar-light">
      <div class="container">
      <a class="navbar-brand" href="/"><img src="images/cropped-favicon.webp" alt="Logo" width = "47" height = "48" class="img-fluid" /></a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown"
        aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNavDropdown">
        <ul class="navbar-nav mx-lg-auto">
            <li class="nav-item">
            <a class="nav-link active" href="/schedule"> View Schedule </a>
            </li>
            <li class="nav-item">
            <a class="nav-link active" href="/about"> About </a>
            </li>
            <li class="nav-item">
            <a class="nav-link active" href="/contact">Contact Us</a>
            </li>
        </ul>
        <a class="btn btn-crimson ms-3" href="/login"> Login </a>
        <a class="btn btn-light ms-3" href="/logout"> Log Out </a>
        </div>
    </div>
  </nav>`

document.getElementsByTagName("header")[0].insertAdjacentHTML("afterbegin", navbar);
