rfcomm-sms
==========

rfcomm-sms is an attempt to provide a serial interface over Bluetooth (for modern Android phones) on which text message (SMS) operations can be executed using old-school AT+C commands. The main reason for this project is the fact that vendors are blocking external access to the internal GSM modem through interfaces like the Bluetooth adapter. While hand-free kits still have access to certain functionalities (eg. phone book), SMS-related AT+C commands are being disabled on several phones and this makes me a sad panda.

To attack this problem, some sources suggest rooting the device. Still doing this has many drawbacks (warenty void, risk of data loss, security issues, ... oh and it's illegal according to the _2012 DMCA_) and also solves only a part of the problem: One might be able to talk to the internal GSM modem from a custom app, but still not from external interfaces. That is why I propose a AT+C command parser in user-space (which emulates the internal GSM modem) and use the Android API to execute the tasks. This might not be the most optimal solution concerning performance, but at least it gets the task done without getting our hands dirty.

Another approach to this problem might be just to write a modern interface using JSON or XML RPC's (in fact, that would be a great idea, because apart from being technical acronyms, they add more buzz to your app, yay!). But some of us have already written libaries which are based on AT+C commands, so with this solution, you can just reuse that code as if you're talking to a good old GSM modem (well... this time with _CRC_ error detection).

Usage
-----

After installing the app, just turn on your Bluetooth adapter and wait for the _SMS over RFCOMM activated_ notification to appear. This indicates that the Bluetooth service will soon be available for your clients. To find out about this service, just use any service discovery tool. For instance on linux there is `sdptool`, which will output the following information:

```
$ sdptool browse 12:34:56:78:9A:BC
Browsing 12:34:56:78:9A:BC...
...
Service Name: RFCOMM SMS
Service RecHandle: 0x10007
Service Class ID List:
  UUID 128: 00001101-0000-1000-8000-00805f9b34fb
Protocol Descriptor List:
  "L2CAP" (0x0100)
  "RFCOMM" (0x0003)
    Channel: 25
...
```

For people that are new to the Bluetooth stack, the following information can be useful:
* The _Service Name_ and SPP (Serial Port Profile) _UUID_ are defined in the `be.dennisdegryse.rfcommsms.client.ListenerService` class. These identify the service, so either in your tests or in your client programs, you should look for these in order to verify the availability and to locate the service;
* The _Protocol Descriptor List_ shows on which layer we are working. The bottom layer is _L2CAP_ (Logical link control and adaptation protocol). On top of that, there is the _RFCOMM_ (Radio frequency communication) layer, which is used to emulate serial ports. This will be the layer on which we interact;
* Nested in the _RFCOMM_ information, you will find the _Channel_. Just like a PC can have multiple serial ports, the _RFCOMM_ layer can have multiple channels (in this case, the service was listening to connections on channel 22). So, in order to connect we must obtain the correct channel number from the _SDP_.

After gathering all information you can test the connection using utilities like `rfcomm` and send some AT+C commands to your phone using a terminal client like `minicom`. If you are new to these tools, just check their `man` pages.

Status
------

The most recent version of the project now supports the following AT+C commands:
* AT : Wave to your modem;
* AT+CMGL : List messages from the phone memory (inbox only);
* AT+CMGR : Read a message from the phone memory (inbox only);
* AT+CMGD : Delete a message from the phone memory;
* AT+CMGS : Send a message.

Also, to receive notifications, the following unsolicited responses are available (currently there is no way to disable them):
* +CTM : Incoming SMS notification.

Issues
------

Apart from being incomplete, this project suffers from the usage of non-standardized material. For instance, the database URI from which to query messages from the phone's storage is not the same on all devices. At the moment there is no generic method to replace this, so it is something to deal with until the Android API defines fixed rules for it. In the mean time, I hope to receive feedback from users with different devices. By confirming that all functionality is working on a specific phone, I can maintain a list of supported devices. Also, by reporting bugs on unsupported phones, they can be investigated and lead to better support in the future. To view the list of supported devices, visit the [Wiki](https://github.com/dennisdegryse/rfcomm-sms/wiki/Supported-Devices)

There is currently no switch for disabling the service, so when you enable your Bluetooth adapter, the service will automatically be enabled with it.

The app listens for Bluetooth adapter state changes, so when you start the app after your Bluetooth adapter has been enabled (for instance after installing it or due to the boot order after starting your phone), it will not start the service (just check whether you received a notification or scan with a service discovery tool). A temporary fix to this problem is just to turn your Bluetooth adapter off and on again.
