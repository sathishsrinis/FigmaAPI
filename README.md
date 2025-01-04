# Figma API Client

This Kotlin project provides a command-line interface (CLI) for interacting with the Figma API. It allows you to download either the full JSON representation of a Figma file or the JSON representation of specific nodes within a file.

## Features

*   **Download Figma File:** Downloads the complete JSON structure of a Figma file.
*   **Download Specific Nodes:** Downloads the JSON structure of specified nodes within a Figma file.
*   **Authentication:** Supports using an access token either from command line arguments or a cached file.
*   **Logging:** Uses SLF4J for detailed logging of operations and errors.

## Prerequisites

*   **Java Development Kit (JDK):**  Make sure you have JDK 11 or higher installed.
*   **Kotlin:** This project is written in Kotlin, and you'll need the Kotlin compiler and build tools setup. (Usually handled by IDE).
*   **Figma Access Token:** You'll need a personal access token from Figma. You can generate one from your Figma account settings under "Personal Access Tokens".

## Setup

1.  **Clone the Repository:**
    ```bash
    git clone <repository-url>
    cd <project-directory>
    ```

2.  **Build the Project:**
    This project is assumed to be a Gradle project, you can build it using gradle wrapper

    ```bash
    ./gradlew build
    ```
    This will generate a JAR file in the `build/libs` directory

## Usage

The application is run from the command line with several options.

### Command Line Options

*   `--file-key <fileKey>`: **Required.** The unique key identifying the Figma file. You can find this in the URL of your Figma file (e.g., `file/FILE_KEY/`).
*   `--node-ids <nodeIds>`: **Required if downloading nodes.** A comma-separated list of node IDs you want to download from the Figma file. These can be found when selecting nodes in figma.
*   `--download-type <type>`: **Optional** Specifies what to download:
    *   `file`: Downloads the entire file. (*Default option when nodes ids are not provided*)
    *   `nodes`: Downloads the specified nodes. (*Default option when nodes ids are provided*)
*   `--access-token <token>`: **Required.** for first time access **Optional.** Your Figma personal access token. If not provided, the application will look for a cached token in the `figma_auth.cache` file.

### Running the Application

Here are some examples of how to run the application:

1.  **Download the entire file:**

    ```bash
    java -jar build/libs/FigmaAPI-1.0-SNAPSHOT-all.jar --file-key <your_file_key> --download-type file
    ```
    This will download the file and save it to `file_response.json`.

2.  **Download specific nodes:**

    ```bash
     java -jar build/libs/FigmaAPI-1.0-SNAPSHOT-all.jar --file-key <your_file_key> --node-ids <node_id1>,<node_id2>,<node_id3> --download-type nodes
    ```
    This will download the specified nodes and save it to `nodes_response.json`.

3.  **Download file using access token from command line**

    ```bash
     java -jar build/libs/FigmaAPI-1.0-SNAPSHOT-all.jar --file-key <your_file_key> --download-type file --access-token <your_access_token>
    ```

4.  **Using cached token (first time use)**

    * Use the example 3 with access token and that will cache it in `figma_auth.cache` for future use

    * After caching, subsequent runs can be done without access token

        ```bash
         java -jar build/libs/FigmaAPI-1.0-SNAPSHOT-all.jar --file-key <your_file_key> --download-type file
        ```

### Output

The application will output a JSON file in the project's root directory:

*   `file_response.json`: If you download the entire file.
*   `nodes_response.json`: If you download specific nodes.

## Logging

The application uses SLF4J with Logback for logging. Log messages will be printed to the console.

## Potential Improvements

*   **Configuration File:** Move the auth cache file name to a configuration file rather than having it hardcoded.
*   **Error Handling:** Add more specific error handling, like handling 404 (Not Found) and rate-limit errors.
*   **Retry Logic:** Implement retry logic with exponential backoff for handling API rate limits.
*   **Progress Indicators:** Consider adding progress indicators or spinners while data is being downloaded.
*   **More API Endpoints:** Expand the application to support more Figma API endpoints (like comments, image exports, etc.).
*   **Testing:** Add unit and integration tests to ensure the application is stable and correct.

## Contributing

Contributions to the project are welcome. Feel free to submit pull requests with improvements or bug fixes.
