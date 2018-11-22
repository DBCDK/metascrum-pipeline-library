#!/usr/bin/env groovy

// Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
// See license text at https://opensource.dbc.dk/licenses/gpl-3.0

def fetchResourceAsText(String url, Map<String, String> headers) {
	URL urlToFetch = findRealUrl(new URL(url), headers)
	return urlToFetch.getText(requestProperties: headers)
}

// taken from: https://stackoverflow.com/a/39725247
def findRealUrl(URL url, Map<String, String> headers) {
	HttpURLConnection conn = url.openConnection()
	conn.followRedirects = false
	conn.requestMethod = "HEAD"
	headers.each { entry ->
		conn.setRequestProperty(entry.key, entry.value)
	}
	if(conn.responseCode in [301,302]) {
		if (conn.headerFields."Location") {
			return findRealUrl(
				conn.headerFields.Location.first().toURL(),
				headers)
		} else {
			throw new RuntimeException("Failed to follow redirect")
		}
	}
	return url
}

// this step fetches a resource, exposes it to a closure and deletes it
// again afterwards
def call(String url, Map headers, steps, Closure body) {
	def content = fetchResourceAsText(url, headers)
	// java/groovy operations on the created file (beyond creating it) doesn't
	// seem to work - File.append, File.delete and others don't fail but have
	// no effects either
	def f = File.createTempFile("tmp", ".tmp")
	// the '' around content is important here - otherwise the script
	// terminates with an error about unexpected newlines
	steps.sh "echo '$content' > ${f.getAbsolutePath()}"
	try {
		body(f.getAbsolutePath())
	} finally {
		steps.sh "rm -f ${f.getAbsolutePath()}"
	}
}
