### CropDoctor

**Description:**\
CropDoctor is an AI-powered Android application for diagnosing common leaf diseases in rice, potato, and corn. It leverages machine learning to analyze leaf images and provides disease identification with an accuracy of 74%.Our project is aimed to help farmers in developing countries by developing an Android Application which will utilize computer vision technology for accurate and cost efficient crop disease detection. You can read our (Project Report)[https://docs.google.com/document/d/1HZjQc1YrYz2NnDgOHM7wYycz_tkXDJTU75hQSuypc7c/edit?tab=t.0] for more details.

#### Features:

-   Detects leaf diseases in rice, potato, and corn
-   Provides accurate, real-time disease diagnosis through image processing

#### Tech Stack:

-   **Language:** Kotlin
-   **Framework:** Android Studio, Jetpack Compose
-   **AI/ML:** Machine Learning Model for Image Classification (deployed within app and trained using Pytorch) 

#### Installation

1.  **Clone the Repository:**

    `git clone https://github.com/aceC0der/CropDoctor.git`

2.  **Open in Android Studio:**

    -   Launch Android Studio.
    -   Select **File > Open** and choose the cloned project directory.
3.  **Configure SDK:**

    -   Ensure you have the latest Android SDK installed.
    -   Go to **File > Project Structure > SDK Location** and set the path to your Android SDK directory if required.
4.  **Build the Project:**

    -   Allow Gradle to sync by clicking **Sync Now** if prompted.
    -   Once synced, build the project by selecting **Build > Make Project**.
5.  **Run the App:**

    -   Connect an Android device via USB (or use an emulator).
    -   Select the device, and click on **Run > Run 'app'**.

#### Usage

1.  Open the app on your Android device.
2.  Take or upload an image of a leaf.
3.  CropDoctor will analyze the image and provide a diagnosis of the leaf disease if detected.
