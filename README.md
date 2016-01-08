# OpenMUC Gateway
This OpenMUC application allows to wire input and output channels to implement gateway applications. Each time the value of a wired input channel has been updated, the value will be written to the corresponding output channel. The application uses the OpenMUC data access service and therefore is completly driver and protocol agnostic. This enable to create gateways between multiple protocol using OpenMUC without any further implementation effort.

## Configuration
For configuration, the application needs an XML file containing the wirings that should be applied. The default file name of the config file is `./conf/wirings.xml`, but can be changed be setting the system property `org.openmucextensions.app.gateway.configFilename`. The following example shows a valid config file:

```
<wirings>
	<wiring>
		<input>input_channel_id</input>
		<output>output_channel_id</output>
	</wiring>
</wirings>
```

The example contains a single wiring defined by the `wiring` element. The sub-elements declare the input channel id as well as the output channel id of the wiring.

The configuration file will be read at bundle activation and all declared wirings will be applied. During runtime, a file watcher recognizes changes in the config file and applies them automatically.
