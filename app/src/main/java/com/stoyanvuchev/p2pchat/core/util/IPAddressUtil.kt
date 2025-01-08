package com.stoyanvuchev.p2pchat.core.util

import android.util.Log
import java.net.Inet4Address
import java.net.NetworkInterface

object IPAddressUtil {

    fun getLocalIPv4Address(): String? {
        try {

            // Get all network interfaces.
            val interfaces = NetworkInterface.getNetworkInterfaces()

            while (interfaces.hasMoreElements()) {
                val networkInterface = interfaces.nextElement()

                // Skip loopback and inactive interfaces.
                if (networkInterface.isLoopback || !networkInterface.isUp) {
                    continue
                }

                // Get all IP addresses for the interface.
                val addresses = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val inetAddress = addresses.nextElement()

                    // Check if it's an IPv4 address and not a loopback address.
                    if (inetAddress is Inet4Address
                        && !(inetAddress.isLoopbackAddress())
                    ) {
                        return inetAddress.getHostAddress()
                    }
                }
            }

        } catch (e: Exception) {
            Log.e(
                "IPAddressUtil: getLocalIPv4Address",
                ": Error: ${e.message}"
            )
        }
        return null
    }

}