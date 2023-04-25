// Get the input element that has the image
const inputElement = document.getElementById("image-input");
const imageElement = document.getElementById("uploaded_image");
const resultImageElement = document.getElementById("result_image");

// Listen for the "change" event on the input element
inputElement.addEventListener("change", handleFiles, false);
//resultImageElement.src = "../Result/result.png"

// Define the handleFiles function
function handleFiles() {
    console.log("Try to send the image")
    const fileList = this.files;
    if (!fileList.length) {
        console.log("No file selected");
        return;
    }
    // Create a new FormData object
    const formData = new FormData();

    // Append the image file to the FormData object
    formData.append("image", fileList[0]);

    //Bild anzeigen
    imageElement.src = URL.createObjectURL(this.files[0])
    var myHeaders = new Headers();

    fetch('/analyze', {
        method: 'POST',
        headers: myHeaders,
        body: formData
    })
        .then(response => response.text())
        .then(data => {
            // Display the text on the HTML page
            console.log(data)
            getGeneratedImage()
        })
        .catch(error => console.log('error', error));
}

function getGeneratedImage() {
    var requestOptions = {
        method: 'GET'
    };

    fetch("/result", requestOptions)
        .then(response => response.blob())
        .then(blob => {
            const imageUrl = URL.createObjectURL(blob);
            resultImageElement.src = imageUrl;
        });
}

