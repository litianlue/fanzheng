package com.dyl.base_lib.util

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.*
import android.os.Parcelable
import android.util.Log

/**
 * Created by dengyulin on 2018/4/21.
 */
class PrintHelp {
    private var mUsbManager: UsbManager? = null
    private var mDevice: UsbDevice? = null
    private var mEndpointIntr: UsbEndpoint? = null
    private var mConnection: UsbDeviceConnection? = null
    private var mPermissionIntent: PendingIntent? = null
    private val ACTION_USB_PERMISSION = "com.first.dyjservice.USB_PERMISSION"
    fun UsbAdmin(context: Context): PrintHelp {
        mUsbManager = context
                .getSystemService(Context.USB_SERVICE) as UsbManager
        mPermissionIntent = PendingIntent.getBroadcast(context, 0, Intent(
                ACTION_USB_PERMISSION), 0)
        val filter = IntentFilter(ACTION_USB_PERMISSION)
        context.registerReceiver(mUsbReceiver, filter)
        // IntentFilter filters = new IntentFilter("com.first.goods");
        // context.registerReceiver(mReciver, filters);
        return this
    }

    private val mUsbReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (ACTION_USB_PERMISSION == action) {
                synchronized(this) {
                    val device = intent
                            .getParcelableExtra<Parcelable>(UsbManager.EXTRA_DEVICE) as UsbDevice
                    if (intent.getBooleanExtra(
                                    UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            setDevice(device)
                        } else {
                        }
                    } else {
                    }

                }

            }
        }
    }

    private fun setDevice(device: UsbDevice?) {
        if (device != null) {
            var intf: UsbInterface? = null
            var ep: UsbEndpoint? = null

            val InterfaceCount = device.interfaceCount
            var j: Int

            mDevice = device
            j = 0
            while (j < InterfaceCount) {
                var i: Int

                intf = device.getInterface(j)
                Log.i("=====", "接口是:" + j + "类是:" + intf!!.interfaceClass)
                if (intf.interfaceClass == 7) {
                    val UsbEndpointCount = intf.endpointCount
                    i = 0
                    while (i < UsbEndpointCount) {
                        ep = intf.getEndpoint(i)
                        // + "端点是:" + i + "方向是:" + ep.getDirection()
                        // + "类型是:" + ep.getType());
                        if (ep!!.direction == 0 && ep.type == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                            // + "接口是:" + j + "端点是:" + i);
                            break
                        }
                        i++
                    }
                    if (i != UsbEndpointCount) {
                        break
                    }
                }
                j++
            }
            if (j == InterfaceCount) {
                // ToastUtil.showMessage(HomeActivity.this, "没有打印机接口");
                return
            }

            mEndpointIntr = ep

            val connection = mUsbManager?.openDevice(device)

            if (connection != null && connection!!.claimInterface(intf, true)) {
                // ToastUtil.showMessage(HomeActivity.this, "打开成功！ ");
                mConnection = connection

            } else {
                // ToastUtil.showMessage(HomeActivity.this, "打开失败！ ");
                mConnection = null
            }
        }

    }

    fun openUsb():PrintHelp {
        if (mDevice != null) {
            setDevice(mDevice)
            if (mConnection == null) {
                val deviceList = mUsbManager
                        ?.getDeviceList()
                val deviceIterator = deviceList?.values
                        ?.iterator()

                while (deviceIterator!!.hasNext()) {
                    val device = deviceIterator.next()
                    mUsbManager?.requestPermission(device, mPermissionIntent)
                }
            }
        } else {
            val deviceList = mUsbManager?.getDeviceList()
            val deviceIterator = deviceList?.values!!.iterator()

            while (deviceIterator.hasNext()) {
                val device = deviceIterator.next()
                mUsbManager?.requestPermission(device, mPermissionIntent)
            }
        }
        return this
    }

    fun sendCommand(Content: ByteArray): Boolean {
        var Result: Boolean = false
        synchronized(this) {
            var len = -1
            if (mConnection != null) {

                len = mConnection?.bulkTransfer(mEndpointIntr, Content,
                        Content.size, 10000) ?: -1
            }

            Result = len >= 0
        }
        return Result
    }

    fun close(context: Context) {
        if (mUsbManager != null) {
            context.unregisterReceiver(mUsbReceiver)
            mConnection?.close()
            mConnection = null
            mUsbManager = null
        }

    }

}