// Get the input element that has the image
const inputElement = document.getElementById("image-input");
const imageElement = document.getElementById("uploaded_image");
const resultImageElement = document.getElementById("result_image");
const loaderElement = document.getElementById("loader");

// Listen for the "change" event on the input element
inputElement.addEventListener("change", preditImage, false);

function preditImage() {
    console.log("Start Object detection")
    //show loader
    loaderElement.classList.remove("hidden");
    loaderElement.classList.add("visible");

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

    fetch('/predict', {
        method: 'POST',
        headers: myHeaders,
        body: formData
    })
    .then(response => response.blob())
    .then(blob => {
        const imageUrl = URL.createObjectURL(blob);
        console.log(imageUrl)
        //disable loader
        loaderElement.classList.remove("visible");
        loaderElement.classList.add("hidden");
        //show image
        resultImageElement.src = imageUrl;
    });
}
