package com.developer.reqwy.myapplication.ocrsdk;
import android.util.Xml;

import java.io.*;
import java.net.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;

public class Client {
	public String applicationId;
	public String password;

	public String serverUrl = "http://cloud.ocrsdk.com";

	/*
	 * Upload image to server and optionally append it to existing task. If
	 * taskId is null, creates new task.
	 */
	public Task submitImage(String filePath, String taskId) throws Exception {
		String taskPart = "";
		if (taskId != null && !taskId.isEmpty()) {
			taskPart = "?taskId=" + taskId;
		}
		URL url = new URL(serverUrl + "/submitImage" + taskPart);

		byte[] fileContents = readDataFromFile(filePath);

		HttpURLConnection connection = openPostConnection(url);

		connection.setRequestProperty("Content-Length",
				Integer.toString(fileContents.length));
		connection.getOutputStream().write(fileContents);

		return getResponse(connection);
	}

	public Task processImage(String filePath, ProcessingSettings settings)
			throws Exception {
		URL url = new URL(serverUrl + "/processImage?" + settings.asUrlParams());
		byte[] fileContents = readDataFromFile(filePath);

		HttpURLConnection connection = openPostConnection(url);

		connection.setRequestProperty("Content-Length",
				Integer.toString(fileContents.length));
		connection.getOutputStream().write(fileContents);

		return getResponse(connection);
	}

	public Task processDocument(String taskId, ProcessingSettings settings)
			throws Exception {
		URL url = new URL(serverUrl + "/processDocument?taskId=" + taskId + "&"
				+ settings.asUrlParams());

		HttpURLConnection connection = openGetConnection(url);
		return getResponse(connection);
	}

	public Task processBusinessCard(String filePath, BusCardSettings settings)
			throws Exception {
		URL url = new URL(serverUrl + "/processBusinessCard?"
				+ settings.asUrlParams());
		byte[] fileContents = readDataFromFile(filePath);

		HttpURLConnection connection = openPostConnection(url);

		connection.setRequestProperty("Content-Length",
				Integer.toString(fileContents.length));
		connection.getOutputStream().write(fileContents);

		return getResponse(connection);
	}

	public Task processTextField(String filePath, TextFieldSettings settings)
			throws Exception {
		URL url = new URL(serverUrl + "/processTextField?"
				+ settings.asUrlParams());
		byte[] fileContents = readDataFromFile(filePath);

		HttpURLConnection connection = openPostConnection(url);

		connection.setRequestProperty("Content-Length",
				Integer.toString(fileContents.length));
		connection.getOutputStream().write(fileContents);

		return getResponse(connection);
	}

	public Task processBarcodeField(String filePath, BarcodeSettings settings)
			throws Exception {
		URL url = new URL(serverUrl + "/processBarcodeField?"
				+ settings.asUrlParams());
		byte[] fileContents = readDataFromFile(filePath);

		HttpURLConnection connection = openPostConnection(url);

		connection.setRequestProperty("Content-Length",
				Integer.toString(fileContents.length));
		connection.getOutputStream().write(fileContents);

		return getResponse(connection);
	}

	public Task processCheckmarkField(String filePath) throws Exception {
		URL url = new URL(serverUrl + "/processCheckmarkField");
		byte[] fileContents = readDataFromFile(filePath);

		HttpURLConnection connection = openPostConnection(url);

		connection.setRequestProperty("Content-Length",
				Integer.toString(fileContents.length));
		connection.getOutputStream().write(fileContents);

		return getResponse(connection);
	}

	/**
	 * Recognize multiple text, barcode and checkmark fields at one call.
	 * 
	 * For details see
	 * http://ocrsdk.com/documentation/apireference/processFields/
	 * 
	 * @param settingsPath
	 *            path to xml file describing processing settings
	 */
	public Task processFields(String taskId, String settingsPath)
			throws Exception {
		URL url = new URL(serverUrl + "/processFields?taskId=" + taskId);
		byte[] fileContents = readDataFromFile(settingsPath);

		HttpURLConnection connection = openPostConnection(url);

		connection.setRequestProperty("Content-Length",
				Integer.toString(fileContents.length));
		connection.getOutputStream().write(fileContents);

		return getResponse(connection);
	}

	public Task getTaskStatus(String taskId) throws Exception {
		URL url = new URL(serverUrl + "/getTaskStatus?taskId=" + taskId);

		HttpURLConnection connection = openGetConnection(url);
		return getResponse(connection);
	}
	
	public String downloadResult(Task task) throws Exception {
		if (task.Status != Task.TaskStatus.Completed) {
			throw new IllegalArgumentException("Invalid task status");
		}

		if (task.DownloadUrl == null) {
			throw new IllegalArgumentException(
					"Cannot download result without url");
		}

		URL url = new URL(task.DownloadUrl);
		// do not use authenticated connection
		URLConnection connection = url.openConnection();
		StringBuffer contents = new StringBuffer();
		Reader reader = new InputStreamReader(connection.getInputStream() ,"UTF-8");
		BufferedReader bufReader = new BufferedReader(reader);
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(bufReader);
		parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
		parser.nextTag();
		parser.nextTag();
		if (parser.getName().equals("field")){
			parser.nextTag();
			if (parser.getName().equals("value")){
				if (parser.next() == XmlPullParser.TEXT) {
					contents.append(parser.getText());
				}
			}
		}
		return contents.toString();
	}

	public void downloadResult(Task task, String outputFile) throws Exception {
		if (task.Status != Task.TaskStatus.Completed) {
			throw new IllegalArgumentException("Invalid task status");
		}

		if (task.DownloadUrl == null) {
			throw new IllegalArgumentException(
					"Cannot download result without url");
		}

		URL url = new URL(task.DownloadUrl);
		// do not use authenticated connection
		URLConnection connection = url.openConnection();

		BufferedInputStream reader = new BufferedInputStream(
				connection.getInputStream());

		FileOutputStream out = new FileOutputStream(outputFile);

		try {
			byte[] data = new byte[1024];
			int count;
			while ((count = reader.read(data, 0, data.length)) != -1) {
				out.write(data, 0, count);
			}
		} finally {
			out.close();
		}
	}
	
	private HttpURLConnection openPostConnection(URL url) throws Exception {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		setupAuthorization(connection);
		connection
				.setRequestProperty("Content-Type", "application/octet-stream");

		return connection;
	}

	private HttpURLConnection openGetConnection(URL url) throws Exception {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		// connection.setRequestMethod("GET");
		setupAuthorization(connection);
		return connection;
	}

	private void setupAuthorization(URLConnection connection) {
		String authString = "Basic " + encodeUserPassword();
		authString = authString.replaceAll("\n", "");
		connection.addRequestProperty("Authorization", authString);
	}

	private byte[] readDataFromFile(String filePath) throws Exception {
		File file = new File(filePath);
		long fileLength = file.length();
		byte[] dataBuffer = new byte[(int) fileLength];

		InputStream inputStream = new FileInputStream(file);

		try {
			int offset = 0;
			while (true) {
				if (offset >= dataBuffer.length) {
					break;
				}
				int numRead = inputStream.read(
						dataBuffer, offset, dataBuffer.length - offset);
				if (numRead < 0) {
					break;
				}
				offset += numRead;
			}
			if (offset < dataBuffer.length) {
				throw new IOException(
						"Could not completely read file " + file.getName());
			}
		} finally {
			inputStream.close();
		}
		return dataBuffer;
	}

	private String encodeUserPassword() {
		String toEncode = applicationId + ":" + password;
		return Base64.encode(toEncode);
	}

	/**
	 * Read server response from HTTP connection and return task description.
	 * 
	 * @throws Exception
	 *             in case of error
	 */
	private Task getResponse(HttpURLConnection connection) throws Exception {
		int responseCode = connection.getResponseCode();
		if (responseCode == 200) {
			InputStream inputStream = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			return new Task(reader);
		} else if (responseCode == 401) {
			throw new Exception(
					"HTTP 401 Unauthorized. Please check your application id and password");
		} else if (responseCode == 407) {
			throw new Exception("HTTP 407. Proxy authentication error");
		} else {
			String message = "";
			try {
				InputStream errorStream = connection.getErrorStream();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(errorStream));

				// Parse xml error response
				InputSource source = new InputSource();
				source.setCharacterStream(reader);
				DocumentBuilder builder = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
				Document doc = builder.parse(source);
				
				NodeList error = doc.getElementsByTagName("error");
				Element err = (Element) error.item(0);
				
				message = err.getTextContent();
			} catch (Exception e) {
				throw new Exception("Error getting server response");
			}

			throw new Exception("Error: " + message);
		}
	}

}
