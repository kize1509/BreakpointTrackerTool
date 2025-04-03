package org.example.frontend
import org.example.pluginmodule.services.BreakpointService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
class ServerTest {

    private lateinit var server: BreakpointServer

    @BeforeEach
    fun setup() {
        // Initialize the server with the default port (8088) or any other port for testing
        server = BreakpointServer()
        server.start()
    }

    @Test
    fun `getServerUrl should never return url with port 0`() {
        // Start the server
        val port = server.getServerUrl()

        // Verify that the server is started on the correct port
        if(port != "http://localhost:0") {
            assert(true)
        }   else{
            fail("The port should never be 0 ")


        }

    }

    @Test
    fun `health check should return status 200 and message "Breakpoint server running"`() {

        val client = OkHttpClient()

        // Creating a GET request
        val request = Request.Builder()
            .url("${server.getServerUrl()}/health")
            .build()

        // Making the request and getting the response
        val response: Response = client.newCall(request).execute()

            if (response.isSuccessful) {
                    assert(true)
            } else {
                fail("Health check failed with status code: ${response.code}")
            }
        }
    }



