package com.n8thnl.mcsr_practice;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import net.minecraft.client.MinecraftClient;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class AdminServer {
    private HttpServer server;
    private final int port = 8081;

    public void start() {
        try {
            // Create the server on localhost:8081
            server = HttpServer.create(new InetSocketAddress(port), 0);

            // Route for the main Admin Page
            server.createContext("/", new DashboardHandler());

            // Route for the Reset Action
            server.createContext("/api/reset", new ResetHandler());

            server.setExecutor(null); // Use default executor
            server.start();
            System.out.println("MCSR Admin Server started on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    // Serves the HTML Dashboard
    static class DashboardHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String html = "<html>" +
                          "<head><title>MCSR Admin</title>" +
                          "<style>body{font-family:sans-serif; text-align:center; padding-top:50px; background:#1e1e1e; color:white;}" +
                          "button{padding:20px 40px; font-size:20px; cursor:pointer; background:#4CAF50; color:white; border:none; border-radius:5px;}" +
                          "button:active{background:#45a049;}</style></head>" +
                          "<body>" +
                          "<h1>MCSR Practice Control</h1>" +
                          "<button onclick=\"fetch('/api/reset', {method: 'POST'})\">Reset & TP to Stronghold</button>" +
                          "<p>Status: Connected</p>" +
                          "</body></html>";

            exchange.sendResponseHeaders(200, html.length());
            OutputStream os = exchange.getResponseBody();
            os.write(html.getBytes());
            os.close();
        }
    }

    // Handles the Logic for the Reset
    static class ResetHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                // Set our flags for the Mixin/TeleportHelper
                MCSRPractice.shouldTeleportToStronghold = true;

                // Trigger Atum on the main Minecraft thread
                MinecraftClient.getInstance().execute(() -> {
                    if (net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("atum")) {
                        me.voidxwalker.autoreset.Atum.scheduleReset();
                    }
                });

                exchange.sendResponseHeaders(200, 0);
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
            exchange.close();
        }
    }
}
