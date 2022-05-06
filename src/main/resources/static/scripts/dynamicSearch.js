let isCacheSupported = 'caches' in window; 
const coursesURL = "/api/get/courses/"

window.addEventListener("DOMContentLoaded", async function () {
    cacheCourses();
    searchCourses();
});

window.addEventListener("keydown", function (event) {
    if (event.key === 'Enter' && validateSearch() === false) {
        event.preventDefault();
        return false;
    }
});

window.onload = function () {
    var filters = document.getElementById("filters");
    var filtersList = filters.getElementsByTagName("input");
    for (var i = 0; i < filtersList.length; i++) {
        filtersList[i].addEventListener("click", function () {
            for (var i = 0; i < filtersList.length; i++) {
                if (filtersList[i] != this && this.checked) {
                    filtersList[i].checked = false;
                }
            }
            displayCourses("Begin typing to search courses");
        });
    }
};

async function cacheCourses() {
    caches.open('dynamicSearchCache').then(cache => {
        cache.add(coursesURL) 
            .then(console.log("Courses added to cache"))
            .catch(error => {
                console.log("Courses Fetch Failed: " + error);
            })
    });
}

function searchCourses() {
    document.getElementById("search").addEventListener("keyup", function (event) {
        if (event.key !== 'Enter') {
            let searchTerm = event.target.value;
            if (searchTerm.length > 0) {
                fetchCourses(searchTerm);
            } else {
                displayCourses("Begin typing to search courses");
            }
        }
    });
}

function fetchCourses(searchTerm) {
    caches.open('dynamicSearchCache').then(cache => {
        cache.match(coursesURL)
            .then(validateJSON)
            .then(courses => filterCourses(courses, searchTerm))
            .then(displayCourses)
            .catch(error => {
                console.log("Query on Cache Failed: " + error);
            })
    });
}

function filterCourses(courses, searchTerm) {
    let courseList = [];

    let codeFilter = document.getElementById("code");
    let keywordFilter = document.getElementById("keyword");
    let timeFilter = document.getElementById("time");
    let daysFilter = document.getElementById("days");

    if (codeFilter.checked) {
        for (const course of courses.courses) {
            if (course.CourseCode.toLowerCase().includes(searchTerm.toLowerCase())) {
                courseList.push(course);
            }
        }
    } else if (keywordFilter.checked) {
        for (const course of courses.courses) {
            if (course.CourseName.toLowerCase().includes(searchTerm.toLowerCase())) {
                courseList.push(course);
            }
        }
    } else if (timeFilter.checked) {
        for (const course of courses.courses) {
            if (course.StartTime.includes(searchTerm)) {
                courseList.push(course);
            }
        }
    } else if (daysFilter.checked) {
        for (const course of courses.courses) {
            if (expand(course.Weekday).toLowerCase().includes(searchTerm.toLowerCase()) || course.Weekday.toLowerCase().includes(searchTerm.toLowerCase())) {
                courseList.push(course);
            } 
        }
    } else { 
        for (const course of courses.courses) {
            if (course.CourseName.toLowerCase().includes(searchTerm.toLowerCase())) {
                courseList.push(course);
            }
        }
    }
    return courseList;
}

function displayCourses(courses) {
    if (courses == "Begin typing to search courses") {
        let empty = document.getElementById("coursesList");
        empty.innerHTML = "";
        let paragraph = document.createElement("p");
        paragraph.innerHTML = courses;
        empty.appendChild(paragraph);
    } else {
        let coursesList = document.getElementById("coursesList");
        coursesList.innerHTML = "";

        if (courses.length == 0) {
            let paragraph = document.createElement("p");
            paragraph.innerHTML = "No courses found";
            coursesList.appendChild(paragraph);
        } else {
            for (const course of courses) {
                let courseItem = document.createElement("input");
                courseItem.type = "checkbox";
                courseItem.name = "courses";
                courseItem.value = JSON.stringify(course);
                coursesList.appendChild(courseItem);

                let label = document.createElement("label");
                label.for = "courses";
                label.innerHTML = course.CourseCode + " | " + course.CourseName;
                if (course.Weekday != "") {
                    label.innerHTML += " | " + course.Weekday;
                }
                if (course.StartTime != "NULL") {
                     label.innerHTML += " | " + course.StartTime + " - " + course.EndTime;
                }
                coursesList.appendChild(label);

                let newLine = document.createElement("br");
                coursesList.appendChild(newLine);
            }
            let courseItem = document.createElement("input");
            courseItem.type = "checkbox";
            courseItem.name = "courses";
            courseItem.value = "{\"CourseID\":99999,\"CourseCode\":\"null101\",\"CourseName\":\"null101\",\"Weekday\":\"MWF\",\"StartTime\":\"01:00:00\",\"EndTime\":\"02:00:00\",\"Enrollment\":30,\"Capacity\":30}";
            courseItem.style.display = "none";
            courseItem.checked = true;
            coursesList.appendChild(courseItem);
        }
    }
}

function expand(weekday) {
    if (weekday == "MWF") {
        return "Monday Wednesday Friday";
    } else if (weekday == "TR") {
        return "Tuesday Thursday";
    } else {
        return weekday;
    }
}

function validateSearch() {
   const checkBoxes = document.getElementsByName("courses");
   var count = 0;
   for (const checkBox of checkBoxes) {
       if (checkBox.checked) {
           count++;
       }

       if (count > 1) {
            return true;
       }
   }
   return false;
}

/**
 * Validate a response to ensure the HTTP status code indcates success.
 * 
 * @author Dr. Brian Dickinson
 * @param {Response} response HTTP response to be checked
 * @returns {object} object encoded by JSON in the response
 */
function validateJSON(response) {
    if (response.ok) {
        return response.json();
    } else {
        return Promise.reject(response);
    }
}