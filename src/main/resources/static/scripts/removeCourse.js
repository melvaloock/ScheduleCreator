window.addEventListener("DOMContentLoaded", function () {
    addButtons();
});

async function addButtons() {
    for (const course of document.querySelectorAll(".event")) {
        course.addEventListener("click", deleteCourse);
    }
}

async function deleteCourse(event) {
    const course = event.target;
    let courseID = course.getAttribute("name");

    let coursesToDelete = document.getElementsByName(courseID);
    console.log(coursesToDelete);
    
    let calendar = document.getElementById("calendar");

    for (var i = 0; i < coursesToDelete.length;) {
        calendar.removeChild(coursesToDelete[i]);
    }
}