let isCacheSupported = 'caches' in window; // may not need this
const coursesURL = "/api/get/courses/"

window.addEventListener("DOMContentLoaded", async function() {
    cacheCourses();
    searchCourses();
});

async function cacheCourses() {
    caches.open('dynamicSearchCache').then(cache => {
        cache.add(coursesURL) // this validates the JSON
            .then(console.log("Courses added to cache"))
            .catch(error => {
                console.log("Courses Fetch Failed: " + error);
            })

        // fetch(coursesURL)
        //     .then(cache.add()
        //     .then(console.log("Courses added to cache"))
        //     .catch(error => {
        //         console.log("Courses Fetch Failed: " + error);
        //     })
    });
}

function searchCourses() {
    document.getElementById("search").addEventListener("keyup", function(event) {
        let searchTerm = event.target.value;
        if (searchTerm.length > 0) {
            fetchCourses(searchTerm);
        } else {
            displayCourses("Begin typing to search courses");
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
    for (const course of courses.courses) {
        if (course.CourseName.toLowerCase().includes(searchTerm.toLowerCase())) {
            courseList.push(course);
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
        console.log(courses);
        let coursesList = document.getElementById("coursesList");
        coursesList.innerHTML = "";

        if (courses.length == 0) {
            let paragraph = document.createElement("p");
            paragraph.innerHTML = "No courses found";
            coursesList.appendChild(paragraph);
        } else {
            for (const course of courses) {
                let courseItem = document.createElement("li");
                courseItem.innerHTML = course.CourseName;
                coursesList.appendChild(courseItem);
            }
        }
    }
}

/**
 * Validate a response to ensure the HTTP status code indcates success.
 * 
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

// function validateAndCache(response) {
//     if (response.ok) {
//         return cache.put(url, response.json());
//     } else {
//         return Promise.reject(response);
//     }
// }