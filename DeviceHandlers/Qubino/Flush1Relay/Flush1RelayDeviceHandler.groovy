/**
 *  Qubino Flush 1 Relay
 *	Device Handler 
 *	Version 1.0
 *  Date: 22.2.2017
 *	Author: Kristjan Jam&scaron;ek (Kjamsek), Goap d.o.o.
 *  Copyright 2017 Kristjan Jam&scaron;ek
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *
 * |---------------------------- DEVICE HANDLER FOR QUBINO FLUSH 1 RELAY Z-WAVE DEVICE -------------------------------------------------------|  
 *	The handler supports all unsecure functions of the Qubino Flush 1 Relay device, except configurable inputs. Configuration parameters and
 *	association groups can be set in the device's preferences screen, but they are applied on the device only after
 *	pressing the 'Set configuration' and 'Set associations' buttons on the bottom of the details view. 
 *
 *	This device handler supports data values that are currently not implemented as capabilities, so custom attribute 
 *	states are used. Please use a SmartApp that supports custom attribute monitoring with this device in your rules.
 * |-----------------------------------------------------------------------------------------------------------------------------------------------|
 *
 *
 *	TO-DO:
 *	- Implement Multichannel Association Command Class to add MC Association functionality and support configurable inputs.
 *  - Implement secure mode
 *
 *	CHANGELOG:
 *	0.99: Final release code cleanup and commenting
 *	1.00: Added comments to code for readability
 */
metadata {
	definition (name: "Qubino Flush 1 Relay", namespace: "Goap", author: "Kristjan Jam&scaron;ek") {
		capability "Actuator"
		capability "Switch"
		capability "Power Meter"
		
		capability "Relay Switch"	// - Tagging capability
		capability "Light"			// - Tagging capability
		
		capability "Configuration" //Needed for configure() function to set MultiChannel Lifeline Association Set
		capability "Temperature Measurement" //This capability is valid for devices with temperature sensors connected
		
		attribute "kwhConsumption", "number" //attribute used to store and display power consumption in KWH 

		command "setConfiguration" //command to issue Configuration Set commands to the module according to user preferences
		command "setAssociation" //command to issue Association Set commands to the modules according to user preferences
		command "refreshPowerConsumption" //command to issue Meter Get requests for KWH measurements from the device, W are already shown as part of Pwer MEter capability
		command "resetPower" //command to issue Meter Reset commands to reset accumulated pwoer measurements

        fingerprint mfr:"0159", prod:"0002", model:"0052", deviceJoinName: "Qubino Flush 1 Relay"  //Manufacturer Information value for Qubino Flush 1 Relay
	}


	simulator {
		// TESTED WITH PHYSICAL DEVICE - UNNEEDED
	}

	tiles(scale: 2) {
		multiAttributeTile(name:"switch", type: "lighting", width: 6, height: 4, canChangeIcon: true){
			tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
				attributeState "on", label:'${name}', action:"switch.off", icon:"st.switches.light.on", backgroundColor:"#79b821", nextState:"turningOff"
				attributeState "off", label:'${name}', action:"switch.on", icon:"st.switches.light.off", backgroundColor:"#ffffff", nextState:"turningOn"
				attributeState "turningOn", label:'${name}', action:"switch.off", icon:"st.switches.light.on", backgroundColor:"#79b821", nextState:"turningOff"
				attributeState "turningOff", label:'${name}', action:"switch.on", icon:"st.switches.light.off", backgroundColor:"#ffffff", nextState:"turningOn"
			}
            tileAttribute ("device.power", key: "SECONDARY_CONTROL") {
				attributeState "power", label:'Power level: ${currentValue} W', icon: "st.Appliances.appliances17"
			}
	    }
		/*
		standardTile("power", "device.power", decoration: "flat", width: 3, height: 3) {
			state("power", label:'${currentValue} W', icon: 'st.Appliances.appliances17')
		}
		*/
		standardTile("kwhConsumption", "device.kwhConsumption", decoration: "flat", width: 6, height: 3) {
			state("kwhConsumption", label:'${currentValue} kWh', icon: 'st.Appliances.appliances17')
		}
		standardTile("resetPower", "device.resetPower", decoration: "flat", width: 3, height: 3) {
			state("resetPower", label:'Reset Power', action:'resetPower')
		}
		standardTile("refreshPowerConsumption", "device.refreshPowerConsumption", decoration: "flat", width: 3, height: 3) {
			state("refreshPowerConsumption", label:'Refresh power', action:'refreshPowerConsumption')
		}
		standardTile("temperature", "device.temperature", width: 6, height: 3) {
			state("temperature", label:'${currentValue} ${unit}', unit:'°', icon: 'st.Weather.weather2', backgroundColors: [
				// Celsius Color Range
				[value: 0, color: "#153591"],
				[value: 7, color: "#1e9cbb"],
				[value: 15, color: "#90d2a7"],
				[value: 23, color: "#44b621"],
				[value: 29, color: "#f1d801"],
				[value: 33, color: "#d04e00"],
				[value: 36, color: "#bc2323"],
				// Fahrenheit Color Range
				[value: 40, color: "#153591"],
				[value: 44, color: "#1e9cbb"],
				[value: 59, color: "#90d2a7"],
				[value: 74, color: "#44b621"],
				[value: 84, color: "#f1d801"],
				[value: 92, color: "#d04e00"],
				[value: 96, color: "#bc2323"]
			])
		}
		
		standardTile("setConfiguration", "device.setConfiguration", decoration: "flat", width: 3, height: 3) {
			state("setConfiguration", label:'Set Configuration', action:'setConfiguration')
		}
		standardTile("setAssociation", "device.setAssociation", decoration: "flat", width: 3, height: 3) {
			state("setAssociation", label:'Set Associations', action:'setAssociation')
		}

		main("switch")
		details(["switch", "temperature", "kwhConsumption", "resetPower", "refreshPowerConsumption", "setConfiguration", "setAssociation"])
	}
	preferences {
/**
*			--------	CONFIGURATION PARAMETER SECTION	--------
*/
				input name: "param1", type: "enum", required: false,
					options: ["0" : "0 - mono-stable switch type (push button)",
							  "1" : "1 - Bi-stable switch type"],
					title: "1. Input 1 switch type.\n " +
						   "0 - Mono-stable switch type.\n" +
						   "1 - Bi-stable switch type.\n" +
						   "Default value: 1"
				
				input name: "param2", type: "enum", required: false,
					options: ["0" : "0 - NO (normally open) input type",
							  "1" : "1 - NC (normally close) input type"],
					title: "2. Input 2 contact type.\n " +
						   "Available settings:\n" +
						   "0 - NO (normally open) input type.\n" +
						   "1 - NC (normally close) input type.\n" +
						   "Default value: 0."
				
				input name: "param3", type: "enum", required: false,
					options: ["0" : "0 - NO (normally open) input type",
							  "1" : "1 - NC (normally close) input type"],
					title: "3. Input 3 contact type.\n " +
						   "Available settings:\n" +
						   "0 - NO (normally open) input type.\n" +
						   "1 - NC (normally close) input type.\n" +
						   "Default value: 0."
					   
				input name: "param10", type: "enum", required: false,
					options: ["0" : "0 - ALL ON is not active, ALL OFF is not active",
							  "1" : "1 - ALL ON is not active, ALL OFF active",
							  "2" : "2 - ALL ON active, ALL OFF is not active",
							  "255" : "255 - ALL ON active, ALL OFF active"],
					title: "10. Activate / deactivate functions ALL ON / ALL OFF.\n " +
						   "Available settings:\n" +
							"255 - ALL ON active, ALL OFF active.\n" +
							"0 - ALL ON is not active, ALL OFF is not active.\n" +
							"1 - ALL ON is not active, ALL OFF active.\n" +
							"2 - ALL ON active, ALL OFF is not active.\n" +
							"Default value: 255.\n" +
							"Flush 1 Relay 0-10V module responds to commands ALL ON / ALL OFF that may be sent by the main controller or by other controller belonging to the system."
				
				input name: "param11", type: "number", range: "0..32535", required: false,
					title: "11. Automatic turning off output after set time.\n " +
						   "Available settings:\n" +
							"0 - Auto OFF disabled.\n" +
							"1 - 32535 = 1 second - 32535 seconds Auto OFF enabled with define time, step is 1 second or 10 ms according to parameter 15.\n" +
							"Default value: 0."
							
				input name: "param12", type: "number", range: "0..32536", required: false,
					title: "12. Automatic turning on output after set time.\n" +
						   "Available settings:\n" +
							"0 - Auto ON disabled.\n" +
							"1 - 32535 = 1second - 32535 seconds Auto ON enabled with define time, step is 1 second.\n" +
							"Default value: 0."
				
				input name: "param15", type: "enum", required: false,
					options: ["0" : "0 - seconds selected",
							  "1" : "1 - milliseconds selected"],
					title: "15. Automatic turning off / on seconds or milliseconds selection.\n " +
						   "Available settings:\n" +
						   "0 - seconds selected.\n" +
						   "1 - milliseconds selected.\n" +
						   "Default value: 0."
							
				input name: "param30", type: "enum", required: false,
					options: ["0" : "0 - Flush 1 Relay module saves its state before power failure (it returns to the last position saved before a power failure)",
							  "1" : "1 - Flush 1 Relay module does not save the state after a power failure, it returns to 'off' position"],
					title: "30. Saving the state of the device after a power failure.\n" +
						   "Available settings:\n" +
							"0 - Flush 1 Relay module saves its state before power failure (it returns to the last position saved before a power failure).\n" +
							"1 - Flush 1 Relay module does not save the state after a power failure, it returns to 'off' position.\n" +
							"Default value: 0."
				
				input name: "param40", type: "number", range: "0..100", required: false,
					title: "40. Power reporting in Watts on power change.\n" +
						   "Set value means percentage, set value from 0 - 100 = 0% - 100%.\n" +
						   "Available settings:\n" +
							"0 - reporting disabled.\n" +
							"1 - 100 = 1% - 100% Reporting enabled. Power report is send (push) only when actual power in Watts in real time changes for more than set percentage comparing to previous actual power in Watts, step is 1%.\n" +
							"Default value: 10 = 10%." +
							"NOTE: if power changed is less than 1W, the report is not send (pushed), independent of percentage set."
							
				input name: "param42", type: "number", range: "0..32535", required: false,
					title: "42. Power reporting in Watts by time interval.\n" +
						   "Set value means time interval (0 - 32535) in seconds, when power report is send.\n" +
						   "Available settings:\n" +
							"0 - reporting disabled.\n" +
							"1 - 32535 = 1 second - 32535 seconds. Reporting enabled. Power report is send with time interval set by entered value.\n" +
							"Default value: 300 = 300 seconds." 

				input name: "param63", type: "enum", required: false,
					options: ["0" : "0 - When system is turned off the output is 0V (NC)",
							  "1" : "1 - When system is turned off the output is 230V or 24V (NO)"],
					title: "63. Output Switch selection.\n" +
						   "Set value means the type of the device that is connected to the output. The device type can be normally open (NO) or normally close (NC).\n" +
						   "Available settings:\n" +
						    "0 - When system is turned off the output is 0V (NC)\n" +
							"1 - When system is turned off the output is 230V or 24V (NO)\n" +
							"Default value: 0."

				input name: "param100", type: "enum", required: false,
					options: ["0" : "0 - Endpoint, I2 disabled",
							  "1" : "1 - Home Security; Motion Detection, unknown location",
							  "2" : "2 - CO; Carbon Monoxide detected, unknown  location",
							  "3" : "3 - CO2; Carbon Dioxide detected, unknown location",
							  "4" : "4 - Water Alarm; Water Leak detected, unknown location",
							  "5" : "5 - Heat Alarm; Overheat detected, unknown location",
							  "6" : "6 - Smoke Alarm; Smoke detected, unknown location",
							  "9" : "9 - Sensor binary"],
					title: "100. Enable / Disable Endpoints I2 or select Notification Type and Event.\n" +
						   "Enabling I2 means that Endpoint (I2) will be present on UI. Disabling it will result in hiding the endpoint according to the parameter set value. Additionally, a Notification Type and Event can be selected for the endpoint.\n" +
						   "Available settings:\n" +
							"0 - Endpoint, I2 disabled\n" +
							"1 - Home Security; Motion Detection, unknown location\n" +
							"2 - CO; Carbon Monoxide detected, unknown  location\n" +
							"3 - CO2; Carbon Dioxide detected, unknown location\n" +
							"4 - Water Alarm; Water Leak detected, unknown location\n" +
							"5 - Heat Alarm; Overheat detected, unknown location\n" +
							"6 - Smoke Alarm; Smoke detected, unknown location\n" +
							"9 - Sensor binary\n" +
							"Default value: 0.\n" +
							"NOTE1: After parameter change, first exclude module (without setting parameters to default value) then wait at least 30s and then re include the module! When the parameter is set to value 9 the notifications are send for Home Security."
							
				input name: "param101", type: "enum", required: false,
					options: ["0" : "0 - Endpoint, I3 disabled",
							  "1" : "1 - Home Security; Motion Detection, unknown location",
							  "2" : "2 - CO; Carbon Monoxide detected, unknown  location",
							  "3" : "3 - CO2; Carbon Dioxide detected, unknown location",
							  "4" : "4 - Water Alarm; Water Leak detected, unknown location",
							  "5" : "5 - Heat Alarm; Overheat detected, unknown location",
							  "6" : "6 - Smoke Alarm; Smoke detected, unknown location",
							  "9" : "9 - Sensor binary"],
					title: "101. Enable / Disable Endpoints I3 or select Notification Type and Event.\n" +
						   "Enabling I3 means that Endpoint (I3) will be present on UI. Disabling it will result in hiding the endpoint according to the parameter set value. Additionally, a Notification Type and Event can be selected for the endpoint.\n" +
						   "Available settings:\n" +
							"0 - Endpoint, I3 disabled\n" +
							"1 - Home Security; Motion Detection, unknown location\n" +
							"2 - CO; Carbon Monoxide detected, unknown  location\n" +
							"3 - CO2; Carbon Dioxide detected, unknown location\n" +
							"4 - Water Alarm; Water Leak detected, unknown location\n" +
							"5 - Heat Alarm; Overheat detected, unknown location\n" +
							"6 - Smoke Alarm; Smoke detected, unknown location\n" +
							"9 - Sensor binary\n" +
							"Default value: 0.\n" +
							"NOTE1: After parameter change, first exclude module (without setting parameters to default value) then wait at least 30s and then re include the module! When the parameter is set to value 9 the notifications are send for Home Security."
							
				input name: "param110", type: "number", range: "1..32536", required: false,
					title: "110. Temperature sensor offset settings.\n" +
						   "Set value is added or subtracted to actual measured value by sensor..\n" +
						   "Available settings:\n" +
							"32536 - offset is 0.0C.\n" +
							"From 1 to 100 - value from 0.1 °C to 10.0 °C is added to actual measured temperature.\n" +
							"From 1001 to 1100 - value from -0.1 °C to -10.0 °C is subtracted to actual measured temperature.\n" +
							"Default value: 32536."

				input name: "param120", type: "number", range: "0..127", required: false,
					title: "120. Digital temperature sensor reporting.\n" +
						   "If digital temperature sensor is connected, module reports measured temperature on temperature change defined by this parameter.\n" +
						   "Available settings:\n" +
							"0 - Reporting disabled.\n" +
							"1 - 127 = 0,1°C - 12,7°C, step is 0,1°C.\n" +
							"Default value: 5 = 0,5°C change."
/*
				input name: "param249", type: "enum", required: false,
					options: ["0" : "0 – Disable reporting.",
							  "1" : "1 – Enable reporting."],
					title: "67. Enable/Disable Reporting on Set command.\n" +
						   "Available settings:\n" +
							"0 – Disable reporting.\n" +
							"1 – Enable reporting.\n" +
							"Default value: 1."
*/			
/**
*			--------	ASSOCIATION GROUP SECTION	--------
*/
				input name: "assocGroup2", type: "text", required: false,
					title: "Association group 2: \n" +
						   "Basic on/off (triggered at change of the output Q state and reflecting it's state) up to 16 nodes.\n" +
						   "NOTE: Insert the node Id value of the devices you wish to associate this group with. Multiple nodeIds can also be set at once by separating individual values by a comma (2,3,...)."
						   
				input name: "assocGroup3", type: "text", required: false,
					title: "Association group 3: \n" +
						   "Basic on/off (triggered at change of the input I2 state and reflecting it's state) up to 16 nodes.\n" +
						   "NOTE: Insert the node Id value of the devices you wish to associate this group with. Multiple nodeIds can also be set at once by separating individual values by a comma (2,3,...)."
						   
				input name: "assocGroup4", type: "text", required: false,
					title: "Association group 4: \n" +
						   "Notification report (triggered at change of the input I2 state and reflecting it's state) up to 16 nodes.\n" +
						   "NOTE: Insert the node Id value of the devices you wish to associate this group with. Multiple nodeIds can also be set at once by separating individual values by a comma (2,3,...)."
						   
				input name: "assocGroup5", type: "text", required: false,
					title: "Association group 5: \n" +
						   "Binary Sensor report (triggered at change of the input I2 state and reflecting it's state) up to 16 nodes.\n" +
						   "NOTE: Insert the node Id value of the devices you wish to associate this group with. Multiple nodeIds can also be set at once by separating individual values by a comma (2,3,...)."
						   
				input name: "assocGroup6", type: "text", required: false,
					title: "Association group 6: \n" +
						   "Basic on/off (triggered at change of the input I3 state and reflecting it's state) up to 16 nodes.\n" +
						   "NOTE: Insert the node Id value of the devices you wish to associate this group with. Multiple nodeIds can also be set at once by separating individual values by a comma (2,3,...)."
						   
				input name: "assocGroup7", type: "text", required: false,
					title: "Association group 7: \n" +
						   "Notification report (triggered at change of the input I3 state and reflecting it's state) up to 16 nodes.\n" +
						   "NOTE: Insert the node Id value of the devices you wish to associate this group with. Multiple nodeIds can also be set at once by separating individual values by a comma (2,3,...)."
						   
				input name: "assocGroup8", type: "text", required: false,
					title: "Association group 8: \n" +
						   "Binary Sensor report (triggered at change of the input I3 state and reflecting it's state) up to 16 nodes.\n" +
						   "NOTE: Insert the node Id value of the devices you wish to associate this group with. Multiple nodeIds can also be set at once by separating individual values by a comma (2,3,...)."
						   
				input name: "assocGroup9", type: "text", required: false,
					title: "Association group 9: \n" +
						   "Multilevel sensor report (triggered at change of temperature sensor) up to 16 nodes.\n" +
						   "NOTE: Insert the node Id value of the devices you wish to associate this group with. Multiple nodeIds can also be set at once by separating individual values by a comma (2,3,...)."
						   
	}
}
/**
*   --------    CHILD DEVICE FUNCTION   --------
*/
/*
def installed() {
        createChildDevices()
        //response(refresh() + configure())
}
private void createChildDevices() {
        // Save the device label for updates by updated()
        state.oldLabel = device.label
        // Add child devices for all five outlets of Zooz Power Strip
        for (i in 1..2) {
        addChildDevice("Flush 1 Relay Sensor", "${device.deviceNetworkId}-${i}", null,[completedSetup: true, label: "${device.displayName} (I${i})", isComponent: false, componentName: "I$i", componentLabel: "I$i"])
        }
}
*/
/**
*	--------	HELPER METHODS SECTION	--------
*/
/**
 * Converts a list of String type node id values to Integer type.
 *
 * @param stringList - a list of String type node id values.
 * @return stringList - a list of Integer type node id values.
*/
def convertStringListToIntegerList(stringList){
	log.debug stringList
	if(stringList != null){
		for(int i=0;i<stringList.size();i++){
			stringList[i] = stringList[i].toInteger()
		}
	}
	return stringList
}
/**
 * Converts temperature values to fahrenheit or celsius scales according to user's setting.
 *
 * @param scaleParam user set scale parameter.
 * @param encapCmd received temperature parsed value.
 * @return String type value of the converted temperature value.
*/
def convertDegrees(scaleParam, encapCmd){
	switch (scaleParam) {
		default:
				break;
		case "F":
				if(encapCmd.scale == 1){
					return encapCmd.scaledSensorValue.toString()
				}else{
					return (encapCmd.scaledSensorValue * 9 / 5 + 32).toString()
				}
				break;
		case "C":
				if(encapCmd.scale == 0){
					return encapCmd.scaledSensorValue.toString()
				}else{
					return (encapCmd.scaledSensorValue * 9 / 5 + 32).toString()
				}
				break;
	}
}
/*
*	--------	HANDLE COMMANDS SECTION	--------
*/
/**
 * Configuration capability command handler.
 *
 * @param void
 * @return List of commands that will be executed in sequence with 500 ms delay inbetween.
*/
def configure() {
	log.debug "Qubino Flush 1 Relay: configure()"
	log.debug "test snippet below:"
	// Parse fingerprint for supported command classes:
	def ccIds = []
	if (getZwaveInfo()?.cc) {
		logger("Device has new-style fingerprint: ${device.rawDescription}","info")
		ccIds = getZwaveInfo()?.cc + getZwaveInfo()?.sec
	}
	else {
		logger("Device has legacy fingerprint: ${device.rawDescription}","info")
		// Look for hexadecimal numbers (0x##) but remove the first one, which will be deviceID.
		ccIds = device.rawDescription.findAll(/0x\p{XDigit}+/)
		if (ccIds.size() > 0) { ccIds.remove(0) }
	}
	ccIds.removeAll([null])
	// Check the device supports MULTI_CHANNEL:
	if (ccIds.find( {it == "0x60" }) ) {
		// Supports MultiChannel...
		log.debug "DEVICE SUPPORTS MULTICHANNEL CC"
	}
	
	state.numEndpoints = 0
	log.debug state.numEndpoints
	def assocCmds = []
	assocCmds << zwave.associationV1.associationSet(groupingIdentifier:1, nodeId:zwaveHubNodeId).format()
	//assocCmds << zwave.multiChannelV3.multiChannelEndPointGet().format()
	return delayBetween(assocCmds, 500)
}
/**
 * Switch capability command handler for ON state. It issues a Switch Binary Set command with value 0xFF.
 * This command is followed by a Switch Binary Get command, that updates the actual state of the device.
 *		
 * @param void
 * @return void.
*/
def on() {
        delayBetween([
				zwave.switchBinaryV1.switchBinarySet(switchValue: 0xFF).format(),
				zwave.switchBinaryV1.switchBinaryGet().format()
        ], 500)  
}
/**
 * Switch capability command handler for ON state. It issues a Switch Binary Set command with value 0x00.
 * This command is followed by a Switch Binary Get command, that updates the actual state of the device.
 *		
 * @param void
 * @return void.
*/
def off() {
        delayBetween([
				zwave.switchBinaryV1.switchBinarySet(switchValue: 0x00).format(),
				zwave.switchBinaryV1.switchBinaryGet().format()
        ], 500)
}
/**
 * Refresh Power Consumption command handler for updating the cumulative consumption fields in kWh. It will issue a Meter Get command with scale parameter set to kWh.
 *		
 * @param void.
 * @return void.
*/
def refreshPowerConsumption() {
	log.debug "Qubino Flush 1 Relay: refreshPowerConsumption()"
	delayBetween([
		zwave.meterV2.meterGet(scale: 0).format(),
		zwave.meterV2.meterGet(scale: 2).format()
    ], 1000)
}
/**
 * setAssociations command handler that sets user selected association groups. In case no node id is insetred the group is instead cleared.
 * Lifeline association hidden from user influence by design.
 *
 * @param void
 * @return List of Association commands that will be executed in sequence with 500 ms delay inbetween.
*/

def setAssociation() {
	log.debug "Qubino Flush 1 Relay: setAssociation()"
	def assocSet = []
	
	//Singlechannel Association group section
	if(settings.assocGroup2 != null){
		def group2parsed = settings.assocGroup2.tokenize(",")
		if(group2parsed == null){
			assocSet << zwave.associationV1.associationSet(groupingIdentifier:2, nodeId:assocGroup2).format()
		}else{
			group2parsed = convertStringListToIntegerList(group2parsed)
			assocSet << zwave.associationV1.associationSet(groupingIdentifier:2, nodeId:group2parsed).format()
		}
	}else{
		assocSet << zwave.associationV2.associationRemove(groupingIdentifier:2).format()
	}
	if(settings.assocGroup3 != null){
		def group3parsed = settings.assocGroup3.tokenize(",")
		if(group3parsed == null){
			assocSet << zwave.associationV1.associationSet(groupingIdentifier:3, nodeId:assocGroup3).format()
		}else{
			group3parsed = convertStringListToIntegerList(group3parsed)
			assocSet << zwave.associationV1.associationSet(groupingIdentifier:3, nodeId:group3parsed).format()
		}
	}else{
		assocSet << zwave.associationV2.associationRemove(groupingIdentifier:3).format()
	}
	if(settings.assocGroup4 != null){
		def group4parsed = settings.assocGroup4.tokenize(",")
		if(group4parsed == null){
			assocSet << zwave.associationV1.associationSet(groupingIdentifier:4, nodeId:assocGroup4).format()
		}else{
			group4parsed = convertStringListToIntegerList(group4parsed)
			assocSet << zwave.associationV1.associationSet(groupingIdentifier:4, nodeId:group4parsed).format()
		}
	}else{
		assocSet << zwave.associationV2.associationRemove(groupingIdentifier:4).format()
	}
	if(settings.assocGroup5 != null){
		def group5parsed = settings.assocGroup5.tokenize(",")
		if(group5parsed == null){
			assocSet << zwave.associationV1.associationSet(groupingIdentifier:5, nodeId:assocGroup5).format()
		}else{
			group5parsed = convertStringListToIntegerList(group5parsed)
			assocSet << zwave.associationV1.associationSet(groupingIdentifier:5, nodeId:group5parsed).format()
		}
	}else{
		assocSet << zwave.associationV2.associationRemove(groupingIdentifier:5).format()
	}
	if(settings.assocGroup6 != null){
		def group6parsed = settings.assocGroup6.tokenize(",")
		if(group6parsed == null){
			assocSet << zwave.associationV1.associationSet(groupingIdentifier:6, nodeId:assocGroup6).format()
		}else{
			group6parsed = convertStringListToIntegerList(group6parsed)
			assocSet << zwave.associationV1.associationSet(groupingIdentifier:6, nodeId:group6parsed).format()
		}
	}else{
		assocSet << zwave.associationV2.associationRemove(groupingIdentifier:6).format()
	}
	if(settings.assocGroup7 != null){
		def group7parsed = settings.assocGroup7.tokenize(",")
		if(group7parsed == null){
			assocSet << zwave.associationV1.associationSet(groupingIdentifier:7, nodeId:assocGroup7).format()
		}else{
			group7parsed = convertStringListToIntegerList(group7parsed)
			assocSet << zwave.associationV1.associationSet(groupingIdentifier:7, nodeId:group7parsed).format()
		}
	}else{
		assocSet << zwave.associationV2.associationRemove(groupingIdentifier:7).format()
	}
	if(settings.assocGroup8 != null){
		def group8parsed = settings.assocGroup8.tokenize(",")
		if(group8parsed == null){
			assocSet << zwave.associationV1.associationSet(groupingIdentifier:8, nodeId:assocGroup8).format()
		}else{
			group8parsed = convertStringListToIntegerList(group8parsed)
			assocSet << zwave.associationV1.associationSet(groupingIdentifier:8, nodeId:group8parsed).format()
		}
	}else{
		assocSet << zwave.associationV2.associationRemove(groupingIdentifier:8).format()
	}
	if(settings.assocGroup9 != null){
		def group9parsed = settings.assocGroup9.tokenize(",")
		if(group9parsed == null){
			assocSet << zwave.associationV1.associationSet(groupingIdentifier:9, nodeId:assocGroup9).format()
		}else{
			group9parsed = convertStringListToIntegerList(group9parsed)
			assocSet << zwave.associationV1.associationSet(groupingIdentifier:9, nodeId:group9parsed).format()
		}
	}else{
		assocSet << zwave.associationV2.associationRemove(groupingIdentifier:8).format()
	}
	/*
	if(settings.ep1McAssocGroup2 != null){
		def ep1McAssocGroup2Parsed = settings.ep1McAssocGroup2.tokenize(",")
		if(ep1McAssocGroup2Parsed != null){ //can contain numbers, number/number
			for (int i = 0; i < ep1McAssocGroup2Parsed.size(); i++) {
				if(ep1McAssocGroup2Parsed[i].contains("/")){ // multichannel combo of node//endpoint
					def ep1McAssocGroup2ParsedSplit = ep1McAssocGroup2Parsed[i].tokenize("/")
					assocSet << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1).encapsulate(zwave.multiChannelAssociationV2.multiChannelAssociationSet(groupingIdentifier: 2, nodeId: [0,ep1McAssocGroup2ParsedSplit[0].toInteger(), ep1McAssocGroup2ParsedSplit[1].toInteger()])).format()
				}else{
					assocSet << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1).encapsulate(zwave.multiChannelAssociationV2.multiChannelAssociationSet(groupingIdentifier: 2, nodeId: [ep1McAssocGroup2Parsed[i].toInteger()])).format()
				}
			}
		}
		else{
			assocSet << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1).encapsulate(zwave.multiChannelAssociationV2.multiChannelAssociationRemove(groupingIdentifier: 2, nodeId: [0])).format()
		}
	}
	else{
		assocSet << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1).encapsulate(zwave.multiChannelAssociationV2.multiChannelAssociationRemove(groupingIdentifier: 2)).format()
	}
	if(settings.ep1McAssocGroup3 != null){
		def ep1McAssocGroup3Parsed = settings.ep1McAssocGroup3.tokenize(",")
		if(ep1McAssocGroup3Parsed != null){ //can contain numbers, number/number
			for (int i = 0; i < ep1McAssocGroup3Parsed.size(); i++) {
				if(ep1McAssocGroup3Parsed[i].contains("/")){ // multichannel combo of node//endpoint
					def ep1McAssocGroup3ParsedSplit = ep1McAssocGroup3Parsed[i].tokenize("/")
					assocSet << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1).encapsulate(zwave.multiChannelAssociationV2.multiChannelAssociationSet(groupingIdentifier: 3, nodeId: [0,ep1McAssocGroup3ParsedSplit[0].toInteger(), ep1McAssocGroup3ParsedSplit[1].toInteger()])).format()
				}else{
					assocSet << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1).encapsulate(zwave.multiChannelAssociationV2.multiChannelAssociationSet(groupingIdentifier: 3, nodeId: [ep1McAssocGroup3Parsed[i].toInteger()])).format()
				}
			}
		}
		else{
			assocSet << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1).encapsulate(zwave.multiChannelAssociationV2.multiChannelAssociationRemove(groupingIdentifier: 3, nodeId: [0])).format()
		}
	}
	else{
		assocSet << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1).encapsulate(zwave.multiChannelAssociationV2.multiChannelAssociationRemove(groupingIdentifier: 3)).format()
	}
	if(settings.ep1McAssocGroup4 != null){
		def ep1McAssocGroup4Parsed = settings.ep1McAssocGroup4.tokenize(",")
		if(ep1McAssocGroup4Parsed != null){ //can contain numbers, number/number
			for (int i = 0; i < ep1McAssocGroup4Parsed.size(); i++) {
				if(ep1McAssocGroup4Parsed[i].contains("/")){ // multichannel combo of node//endpoint
					def ep1McAssocGroup4ParsedSplit = ep1McAssocGroup4Parsed[i].tokenize("/")
					assocSet << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1).encapsulate(zwave.multiChannelAssociationV2.multiChannelAssociationSet(groupingIdentifier: 4, nodeId: [0,ep1McAssocGroup4ParsedSplit[0].toInteger(), ep1McAssocGroup4ParsedSplit[1].toInteger()])).format()
				}else{
					assocSet << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1).encapsulate(zwave.multiChannelAssociationV2.multiChannelAssociationSet(groupingIdentifier: 4, nodeId: [ep1McAssocGroup4Parsed[i].toInteger()])).format()
				}
			}
		}
		else{
			assocSet << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1).encapsulate(zwave.multiChannelAssociationV2.multiChannelAssociationRemove(groupingIdentifier: 4, nodeId: [0])).format()
		}
	}
	else{
		assocSet << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1).encapsulate(zwave.multiChannelAssociationV2.multiChannelAssociationRemove(groupingIdentifier: 4)).format()
	}
	if(settings.ep2McAssocGroup2 != null){
		def ep2McAssocGroup2Parsed = settings.ep2McAssocGroup2.tokenize(",")
		if(ep2McAssocGroup2Parsed != null){ //can contain numbers, number/number
			for (int i = 0; i < ep2McAssocGroup2Parsed.size(); i++) {
				if(ep2McAssocGroup2Parsed[i].contains("/")){ // multichannel combo of node//endpoint
					def ep2McAssocGroup2ParsedSplit = ep2McAssocGroup2Parsed[i].tokenize("/")
					assocSet << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:2).encapsulate(zwave.multiChannelAssociationV2.multiChannelAssociationSet(groupingIdentifier: 2, nodeId: [0,ep2McAssocGroup2ParsedSplit[0].toInteger(), ep2McAssocGroup2ParsedSplit[1].toInteger()])).format()
				}else{
					assocSet << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:2).encapsulate(zwave.multiChannelAssociationV2.multiChannelAssociationSet(groupingIdentifier: 2, nodeId: [ep2McAssocGroup2Parsed[i].toInteger()])).format()
				}
			}
		}
		else{
			assocSet << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:2).encapsulate(zwave.multiChannelAssociationV2.multiChannelAssociationRemove(groupingIdentifier: 2, nodeId: [0])).format()
		}
	}
	else{
		assocSet << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:2).encapsulate(zwave.multiChannelAssociationV2.multiChannelAssociationRemove(groupingIdentifier: 2)).format()
	}

	if(settings.ep3McAssocGroup2 != null){
		def ep3McAssocGroup2Parsed = settings.ep3McAssocGroup2.tokenize(",")
		if(ep3McAssocGroup2Parsed != null){ //can contain numbers, number/number
			for (int i = 0; i < ep3McAssocGroup2Parsed.size(); i++) {
				if(ep3McAssocGroup2Parsed[i].contains("/")){ // multichannel combo of node//endpoint
					def ep3McAssocGroup2ParsedSplit = ep3McAssocGroup2Parsed[i].tokenize("/")
					assocSet << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:3).encapsulate(zwave.multiChannelAssociationV2.multiChannelAssociationSet(groupingIdentifier: 2, nodeId: [0,ep3McAssocGroup2ParsedSplit[0].toInteger(), ep3McAssocGroup2ParsedSplit[1].toInteger()])).format()
				}else{
					assocSet << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:3).encapsulate(zwave.multiChannelAssociationV2.multiChannelAssociationSet(groupingIdentifier: 2, nodeId: [ep3McAssocGroup2Parsed[i].toInteger()])).format()
				}
			}
		}
		else{
			assocSet << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:3).encapsulate(zwave.multiChannelAssociationV2.multiChannelAssociationRemove(groupingIdentifier: 2, nodeId: [0])).format()
		}
	}
	else{
		assocSet << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:3).encapsulate(zwave.multiChannelAssociationV2.multiChannelAssociationRemove(groupingIdentifier: 2)).format()
	}
	*/
	if(assocSet.size() > 0){
		return delayBetween(assocSet, 500)
	}
}

/**
 * setConfigurationParams command handler that sets user selected configuration parameters on the device. 
 * In case no value is set for a specific parameter the method skips setting that parameter.
 * Secure mode setting hidden from user influence by design.
 *
 * @param void
 * @return List of Configuration Set commands that will be executed in sequence with 500 ms delay inbetween.
*/

def setConfiguration() {
	log.debug "Qubino Flush 1 Relay: setConfiguration()"
	def configSequence = []
	if(settings.param1 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 1, size: 1, scaledConfigurationValue: settings.param1.toInteger()).format()
	}
	if(settings.param2 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 2, size: 1, scaledConfigurationValue: settings.param2.toInteger()).format()
	}
	if(settings.param3 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 3, size: 1, scaledConfigurationValue: settings.param3.toInteger()).format()
	}
	if(settings.param10 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 10, size: 2, scaledConfigurationValue: settings.param10.toInteger()).format()
	}
	if(settings.param11 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 11, size: 2, scaledConfigurationValue: settings.param11.toInteger()).format()
	}
	if(settings.param12 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 12, size: 2, scaledConfigurationValue: settings.param12.toInteger()).format()
	}
	if(settings.param15 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 15, size: 1, scaledConfigurationValue: settings.param15.toInteger()).format()
	}
	if(settings.param30 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 30, size: 1, scaledConfigurationValue: settings.param30.toInteger()).format()
	}
	if(settings.param40 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 40, size: 1, scaledConfigurationValue: settings.param40.toInteger()).format()
	}
	if(settings.param42 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 42, size: 2, scaledConfigurationValue: settings.param42.toInteger()).format()
	}
	if(settings.param63 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 63, size: 1, scaledConfigurationValue: settings.param63.toInteger()).format()
	}
	if(settings.param100 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 100, size: 1, scaledConfigurationValue: settings.param100.toInteger()).format()
	}
	if(settings.param101 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 101, size: 1, scaledConfigurationValue: settings.param101.toInteger()).format()
	}
	if(settings.param110 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 110, size: 2, scaledConfigurationValue: settings.param110.toInteger()).format()
	}	
	if(settings.param120 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 120, size: 1, scaledConfigurationValue: settings.param120.toInteger()).format()
	}	
	if(configSequence.size() > 0){
		return delayBetween(configSequence, 500)
	}
}


/*
*	--------	EVENT PARSER SECTION	--------
*/
/**
 * parse function takes care of parsing received bytes and passing them on to event methods.
 *
 * @param description String type value of the received bytes.
 * @return Parsed result of the received bytes.
*/
def parse(String description) {
	log.debug "Qubino Flush 1 Relay: Parsing '${description}'"
	def result = null
    def cmd = zwave.parse(description)
    if (cmd) {
		result = zwaveEvent(cmd)
        log.debug "Parsed ${cmd} to ${result.inspect()}"
    } else {
		log.debug "Non-parsed event: ${description}"
    }
    return result
}
/**
 * Event handler for received Sensor Multilevel Report frames. These are for the temperature sensor connected to TS connector.
 *
 * @param void
 * @return Event that updates the temperature values with received values.
*/
def zwaveEvent(physicalgraph.zwave.commands.sensormultilevelv5.SensorMultilevelReport cmd){
	log.debug "Qubino Flush 1 Relay: SensorMultilevelReport handler fired"
	def resultEvents = []
	resultEvents << createEvent(name:"temperature", value: convertDegrees(location.temperatureScale,cmd), unit:"°"+location.temperatureScale, descriptionText: "Temperature: "+convertDegrees(location.temperatureScale,cmd)+"°"+location.temperatureScale)
	return resultEvents
}
/**
 * Event handler for received Switch Binary Report frames. Used for ON / OFF events.
 *
 * @param void
 * @return Switch Event with on or off value.
*/
def zwaveEvent(physicalgraph.zwave.commands.switchbinaryv1.SwitchBinaryReport cmd) {
	log.debug "firing switch binary report event"
    createEvent(name:"switch", value: cmd.value ? "on" : "off")
}
/**
 * Event handler for received Configuration Report frames. Used for debugging purposes. 
 *
 * @param void
 * @return void.
*/
def zwaveEvent(physicalgraph.zwave.commands.configurationv2.ConfigurationReport cmd){
	log.debug "firing configuration report event"
	log.debug cmd.configurationValue
}
/**
 * Event handler for received MC Encapsulated Meter Report frames.
 *
 * @param void
 * @return List of events to update the ON / OFF and analogue control elements with received values.
*/
def zwaveEvent(physicalgraph.zwave.commands.meterv3.MeterReport cmd, physicalgraph.zwave.commands.multichannelv3.MultiChannelCmdEncap command){
	log.debug "Qubino Flush 1 Relay: firing meter report event"
	def result = []
	switch(cmd.scale){
		case 0:
			result << createEvent(name:"kwhConsumption", value: cmd.scaledMeterValue, unit:"kWh", descriptionText:"${device.displayName} consumed ${cmd.scaledMeterValue} kWh")
			break;
		case 2:
			result << createEvent(name:"power", value: cmd.scaledMeterValue, unit:"W", descriptionText:"${device.displayName} consumes ${cmd.scaledMeterValue} W")
			break;
	}
	return result
	return result
}
/**
 * Event handler for received Meter Report frames. Used for displaying W and kWh measurements.
 *
 * @param void
 * @return Power consumption event for W data or kwhConsumption event for kWh data.
*/
def zwaveEvent(physicalgraph.zwave.commands.meterv3.MeterReport cmd) {
	log.debug "Qubino Flush 1 Relay: firing meter report event"
	def result = []
	switch(cmd.scale){
		case 0:
			result << createEvent(name:"kwhConsumption", value: cmd.scaledMeterValue, unit:"kWh", descriptionText:"${device.displayName} consumed ${cmd.scaledMeterValue} kWh")
			break;
		case 2:
			result << createEvent(name:"power", value: cmd.scaledMeterValue, unit:"W", descriptionText:"${device.displayName} consumes ${cmd.scaledMeterValue} W")
			break;
	}
	return result
}
/**
 * Event handler for received MultiChannelEndPointReport commands. Used to distinguish when the device is in singlechannel or multichannel configuration. 
 *
 * @param cmd communication frame
 * @return commands to set up a MC Lifeline association.
*/
def zwaveEvent(physicalgraph.zwave.commands.multichannelv3.MultiChannelEndPointReport cmd){
	log.debug "Qubino Flush Shutter DC: firing MultiChannelEndPointReport"
	if(cmd.endPoints > 0){
		state.numEndpoints = cmd.endPoints;
	}

	def cmds = []
	cmds << response(zwave.associationV1.associationRemove(groupingIdentifier:1).format())
	cmds << response(zwave.multiChannelAssociationV2.multiChannelAssociationSet(groupingIdentifier: 1, nodeId: [0,zwaveHubNodeId,1]).format())
	return cmds
}
/**
 * Event handler for received Multi Channel Encapsulated commands.
 *
 * @param cmd encapsulated communication frame
 * @return parsed event.
*/
def zwaveEvent(physicalgraph.zwave.commands.multichannelv3.MultiChannelCmdEncap cmd){
	log.debug "Qubino Flush Shutter DC: firing MC Encapsulation event"
	def encapsulatedCommand = cmd.encapsulatedCommand()
	//log.debug ("Command from endpoint ${cmd.sourceEndPoint}: ${encapsulatedCommand}")
	if (encapsulatedCommand) {
			return zwaveEvent(encapsulatedCommand, cmd)
	}
}
def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd){
	log.debug "Qubino Flush 1 Relay: firing basic report event"
	log.debug cmd
}
