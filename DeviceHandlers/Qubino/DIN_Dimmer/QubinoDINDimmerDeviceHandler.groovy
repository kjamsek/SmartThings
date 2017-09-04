/**
 *  Qubino DIN Dimmer
 *	Device Handler 
 *	Version 1.0
 *  Date: 4.9.2017
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
 * |---------------------------- DEVICE HANDLER FOR QUBINO DIN DIMMER Z-WAVE DEVICE -------------------------------------------------------|  
 *	The handler supports all unsecure functions of the Qubino DIN Dimmer device, except configurable inputs. Configuration parameters and
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
	definition (name: "Qubino DIN Dimmer", namespace: "Goap", author: "Kristjan Jam&scaron;ek") {
		capability "Actuator"
		capability "Switch"
		capability "Switch Level"
		capability "Power Meter"
		
		capability "Relay Switch"	// - Tagging capability
		capability "Light"			// - Tagging capability
		capability "Sensor"			// - Tagging capability for configurable inputs
		
		capability "Configuration" //Needed for configure() function to set any specific configurations
		capability "Temperature Measurement" //This capability is valid for devices with temperature sensors connected
		
		attribute "kwhConsumption", "number" //attribute used to store and display power consumption in KWH 

		command "setConfiguration" //command to issue Configuration Set commands to the module according to user preferences
		command "setAssociation" //command to issue Association Set commands to the modules according to user preferences
		command "refreshPowerConsumption" //command to issue Meter Get requests for KWH measurements from the device, W are already shown as part of Pwer MEter capability
		command "resetPower" //command to issue Meter Reset commands to reset accumulated pwoer measurements
		
        fingerprint mfr:"0159", prod:"0001", model:"0052"  //Manufacturer Information value for Qubino DIN Dimmer
	}


	simulator {
		// TESTED WITH PHYSICAL DEVICE - UNNEEDED
	}
	tiles(scale: 2) {
		multiAttributeTile(name:"switch", type: "lighting", width: 6, height: 4, canChangeIcon: true){
			tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
				attributeState "on", label:'${name}', action:"switch.off", icon:"st.switches.light.on", backgroundColor:"#00a0dc", nextState:"turningOff"
				attributeState "off", label:'${name}', action:"switch.on", icon:"st.switches.light.off", backgroundColor:"#ffffff", nextState:"turningOn"
				attributeState "turningOn", label:'${name}', action:"switch.off", icon:"st.switches.light.on", backgroundColor:"#79b821", nextState:"turningOff"
				attributeState "turningOff", label:'${name}', action:"switch.on", icon:"st.switches.light.off", backgroundColor:"#ffffff", nextState:"turningOn"
			}
			tileAttribute ("device.level", key: "SLIDER_CONTROL") {
				attributeState "level", action:"switch level.setLevel"
			}
            tileAttribute ("device.power", key: "SECONDARY_CONTROL") {
				attributeState "power", label:'Power level: ${currentValue} W', icon: "st.Appliances.appliances17"
			}
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
		standardTile("power", "device.power", decoration: "flat", width: 3, height: 3) {
			state("power", label:'${currentValue} W', icon: 'st.Appliances.appliances17')
		}
		standardTile("kwhConsumption", "device.kwhConsumption", decoration: "flat", width: 3, height: 3) {
			state("kwhConsumption", label:'${currentValue} kWh', icon: 'st.Appliances.appliances17')
		}
		standardTile("resetPower", "device.resetPower", decoration: "flat", width: 3, height: 3) {
			state("resetPower", label:'Reset Power', action:'resetPower')
		}
		standardTile("refreshPowerConsumption", "device.refreshPowerConsumption", decoration: "flat", width: 3, height: 3) {
			state("refreshPowerConsumption", label:'Refresh power', action:'refreshPowerConsumption')
		}
		
		standardTile("setConfiguration", "device.setConfiguration", decoration: "flat", width: 3, height: 3) {
			state("setConfiguration", label:'Set Configuration', action:'setConfiguration')
		}
		standardTile("setAssociation", "device.setAssociation", decoration: "flat", width: 3, height: 3) {
			state("setAssociation", label:'Set Associations', action:'setAssociation')
		}
		
		main("switch")
		details(["switch", "temperature", "power", "kwhConsumption", "resetPower", "refreshPowerConsumption", "setConfiguration", "setAssociation"])
		
	}
	
	preferences {
/**
*			--------	CONFIGURATION PARAMETER SECTION	--------
*/
				input (
					type: "paragraph",
					element: "paragraph",
					title: "CONFIGURATION PARAMETERS:",
					description: "Configuration parameter settings."
				)
				input name: "param1", type: "enum", required: false,
					options: ["0" : "0 - mono-stable switch type (push button)",
							  "1" : "1 - Bi-stable switch type"],
					title: "1. Input 1 switch type.\n " +
						   "Available settings:\n" +
						   "0 - button quick press turns between previous set Dimmer value and zero.\n" +
						   "1 - Bi-stable switch type.\n" +
						   "Default value: 0."
				
				input name: "param5", type: "enum", required: false,
					options: ["0" : "0 - Dimmer mode",
							  "1" : "1 - Switch mode"],
					title: "5.With this parameter is possible to change the module the way the module functions.\n " +
						   "Available settings:\n" +
						   "0 - Dimmer mode.\n" +
						   "1 - Switch mode.\n" +
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
							"DIN Dimmer module responds to commands ALL ON / ALL OFF that may be sent by the main controller or by other controller belonging to the system."
				
				input name: "param11", type: "number", range: "0..32536", required: false,
					title: "11. Automatic turning off output after set time.\n " +
						   "Available settings:\n" +
							"0 - Auto OFF disabled.\n" +
							"1 - 32536 = 1 second - 32536 seconds Auto OFF enabled with defined time, step is 1 second.\n" +
							"Default value: 0."
							
				input name: "param12", type: "number", range: "0..32536", required: false,
					title: "12. Automatic turning on output after set time.\n" +
						   "Available settings:\n" +
							"0 - Auto ON disabled.\n" +
							"1 - 32536 = 1 second - 32536 seconds Auto ON enabled with define time, step is 1 second.\n" +
							"Default value: 0."

				input name: "param21", type: "enum", required: false,
					options: ["0" : "0 - Double click disabled",
							  "1" : "1 - Double click enabled"],
					title: "21. Enable/Disable Double click function.\n" +
						   "If Double click function is enabled, a fast double click on the push button will set dimming power at maximum dimming value.\n" +
						   "Available settings:\n" +
							"0 - Double click disabled.\n" +
							"1 - Double click enabled.\n" +
							"Default value: 0."
							
				input name: "param30", type: "enum", required: false,
					options: ["0" : "0 - DIN Dimmer module saves its state before power failure (it returns to the last position saved before a power failure)",
							  "1" : "1 - DIN Dimmer module does not save the state after a power failure, it returns to 'off' position"],
					title: "30. Saving the state of the device after a power failure.\n" +
						   "Available settings:\n" +
							"0 - DIN Dimmer module saves its state before power failure (it returns to the last position saved before a power failure).\n" +
							"1 - DIN Dimmer module does not save the state after a power failure, it returns to 'off' position.\n" +
							"Default value: 0."
				
				input name: "param40", type: "number", range: "0..100", required: false,
					title: "40. Power reporting in Watts on power change.\n" +
						   "Set value means percentage, set value from 0 - 100 = 0% - 100%.\n" +
						   "Available settings:\n" +
							"0 - reporting disabled.\n" +
							"1 - 100 = 1% - 100% Reporting enabled. Power report is send (push) only when actual power in Watts in real time changes for more than set percentage comparing to previous actual power in Watts, step is 1%.\n" +
							"Default value: 5.\n" +
							"NOTE: if power changed is less than 1W, the report is not send (pushed), independent of percentage set."
							
				input name: "param42", type: "number", range: "0..32767", required: false,
					title: "42. Power reporting in Watts by time interval.\n" +
						   "Set value means time interval (0 - 32767) in seconds, when power report is send.\n" +
						   "Available settings:\n" +
							"0 - reporting disabled.\n" +
							"1 - 32767 = 1 second - 32767 seconds. Reporting enabled. Power report is send with time interval set by entered value.\n" +
							"Default value: 0."

				input name: "param60", type: "number", range: "1..98", required: false,
					title: "60. Minimum dimming value.\n" +
						   "Available settings:\n" +
							"1 - 98 = 1% - 98%, step is 1%. Minimum dimming values is set by entered value.\n" +
							"Default value: 1 (1%).\n" +
							"NOTE: The minimum level may not be higher than the maximum level! 1% min. dimming value is defined by Z-Wave multilevel device class. When the switch type is selected as Bi-stable, it is not possible to dim the value between min and max. If Switch_multilevel_set is set to the value “0”, the output is turned OFF. If Switch_multilevel_set is set to the value “1”, the output is set to the minimum diming value."

				input name: "param61", type: "number", range: "1..99", required: false,
					title: "61. Maximum dimming value.\n" +
						   "Available settings:\n" +
							"2 - 99 = 2% - 99%, step is 1%. Maximum dimming values is set by entered value.\n" +
							"Default value: 99 (99%).\n" +
							"NOTE: The maximum level may not be lower than the minimum level! 99% max. dimming value is defined by Z-Wave multilevel device class. When the switch type is selected as Bi-stable, it is not possible to dim the value between min and max."

				input name: "param65", type: "number", range: "50..255", required: false,
					title: "65. Dimming time (soft on/off).\n" +
						   "Set value means time of moving the Dimmer between min. and max. dimming values by short press of push button I1 or controlled through UI (BasicSet).\n" +
						   "Available settings:\n" +
							"50 - 255 = 500 mseconds - 2550 mseconds (2,55s), step is 10 mseconds.\n" +
							"Default value: 100 (1s).\n" +
							"NOTE: The maximum level may not be lower than the minimum level! 99% max. dimming value is defined by Z-Wave multilevel device class. When the switch type is selected as Bi-stable, it is not possible to dim the value between min and max."

				input name: "param66", type: "number", range: "1..255", required: false,
					title: "66. Dimming time when key pressed.\n" +
						   "Time of moving the Dimmer between min. and max dimming values by continues hold of push button I1 or associated device.\n" +
						   "Available settings:\n" +
							"1 - 255 = 1 second - 255 seconds.\n" +
							"Default value: 3 (3s)."

				input name: "param67", type: "enum", required: false,
					options: ["0" : "0 - (respect start level)",
							  "1" : "1 - (ignore start level)"],
					title: "67. Ignore start level.\n" +
						   "This parameter is used with association group 3.\n" +
						   "A receiving device SHOULD respect the start level if the Ignore Start Level bit is 0. A receiving device MUST ignore the start level if the Ignore Start Level bit is 1.\n" +
						   "Available settings:\n" +
							"0 - (respect start level).\n" +
							"1 - (ignore start level).\n" +
							"Default value: 0."

				input name: "param68", type: "number", range: "0..127", required: false,
					title: "68. Dimming duration.\n" +
						   "This parameter is used with association group 3.\n" +
						   "The Duration field MUST specify the time that the transition should take from the current value to the new target value. A supporting device SHOULD respect the specified Duration value.\n" +
						   "Available settings:\n" +
							"0 – 127 (from 1 to 127 seconds).\n" +
							"Default value: 0 (dimming duration according to parameter 66)."

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
			
/**
*			--------	ASSOCIATION GROUP SECTION	--------
*/
				input (
					type: "paragraph",
					element: "paragraph",
					title: "ASSOCIATION GROUPS:",
					description: "Association group settings."
				)
				input name: "assocGroup2", type: "text", required: false,
					title: "Association group 2: \n" +
						   "Basic on/off (triggered at change of the input I1 and reflecting state of the output Q) up to 16 nodes.\n" +
						   "NOTE: Insert the node Id value of the devices you wish to associate this group with. Multiple nodeIds can also be set at once by separating individual values by a comma (2,3,...)."
						   
				input name: "assocGroup3", type: "text", required: false,
					title: "Association group 3: \n" +
						   "Start level change/stop level change (triggered at change of the input I1 state and reflecting its state) up to 16 nodes." +
						   "NOTE: Insert the node Id value of the devices you wish to associate this group with. Multiple nodeIds can also be set at once by separating individual values by a comma (2,3,...)."
						   
				input name: "assocGroup4", type: "text", required: false,
					title: "Association group 4: \n" +
						   "Multilevel set (triggered at changes of state/value of the DIN Dimmer) up to 16 nodes." +
						   "NOTE: Insert the node Id value of the devices you wish to associate this group with. Multiple nodeIds can also be set at once by separating individual values by a comma (2,3,...)."
						   
				input name: "assocGroup5", type: "text", required: false,
					title: "Association group 5: \n" +
						   "Multilevel Sensor Report (triggered at the change od temperature sensor values) up to 16 nodes.\n" +
						   "NOTE: Insert the node Id value of the devices you wish to associate this group with. Multiple nodeIds can also be set at once by separating individual values by a comma (2,3,...)."
						   
	}
}
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
	log.debug "Qubino DIN Dimmer: configure()"
	def cmds = []
	cmds << zwave.associationV1.associationRemove(groupingIdentifier:1).format()
	cmds << zwave.associationV1.associationSet(groupingIdentifier:1, nodeId:zwaveHubNodeId).format()
	cmds << zwave.multiChannelV3.multiChannelEndPointGet().format()
	return response(delayBetween(cmds, 1000))
	
}

/**
 * Switch capability command handler for ON state. It issues a Switch Multilevel Set command with value 0xFF and instantaneous dimming duration.
 * This command is followed by a Switch Multilevel Get command, that updates the actual state of the dimmer.
 *		
 * @param void
 * @return void.
*/
def on() {
        delayBetween([
				zwave.switchMultilevelV3.switchMultilevelSet(value: 0xFF, dimmingDuration: 0x00).format(),
				zwave.switchMultilevelV1.switchMultilevelGet().format()
        ], 1000)  
}
/**
 * Switch capability command handler for OFF state. It issues a Switch Multilevel Set command with value 0x00 and instantaneous dimming duration.
 * This command is followed by a Switch Multilevel Get command, that updates the actual state of the dimmer.
 *		
 * @param void
 * @return void.
*/
def off() {
        delayBetween([
				zwave.switchMultilevelV3.switchMultilevelSet(value: 0x00, dimmingDuration: 0x00).format(),
				zwave.switchMultilevelV1.switchMultilevelGet().format()
        ], 1000)
}
/**
 * Switch Level capability command handler for a positive dimming state. It issues a Switch Multilevel Set command with value contained in the parameter value and instantaneous dimming duration.
 * This command is followed by a Switch Multilevel Get command, that updates the actual state of the dimmer. We need to limit the max valueto 99% by Z-Wave protocol definitions.
 *		
 * @param level The desired value of the dimmer we are trying to set.
 * @return void.
*/
def setLevel(level) {
	if(level > 99) level = 99
    delayBetween([
		zwave.switchMultilevelV3.switchMultilevelSet(value: level, dimmingDuration: 0x00).format(),
		zwave.switchMultilevelV1.switchMultilevelGet().format()
    ], 1000)
}

/**
 * Refresh Power Consumption command handler for updating the cumulative consumption fields in kWh. It will issue a Meter Get command with scale parameter set to kWh.
 *		
 * @param void.
 * @return void.
*/
def refreshPowerConsumption() {
	log.debug "Qubino DIN Dimmer: refreshPowerConsumption()"
	delayBetween([
		zwave.meterV2.meterGet(scale: 0).format(),
		zwave.meterV2.meterGet(scale: 2).format()
    ], 1000)
}
/**
 * Reset Power Consumption command handler for resetting the cumulative consumption fields in kWh. It will issue a Meter Reset command followed by Meter Get commands for active and accumulated power.
 *		
 * @param void.
 * @return void.
*/
def resetPower() {
	log.debug "Qubino DIN Dimmer: resetPower()"
	zwave.meterV2.meterReset()
	delayBetween([
		zwave.meterV2.meterReset(),
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
	log.debug "Qubino DIN Dimmer: setAssociation()"
	def assocSet = []
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
	log.debug "Qubino DIN Dimmer: setConfiguration()"
	def configSequence = []
	if(settings.param1 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 1, size: 1, scaledConfigurationValue: settings.param1.toInteger()).format()
	}
	if(settings.param5 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 5, size: 1, scaledConfigurationValue: settings.param5.toInteger()).format()
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
	if(settings.param21 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 21, size: 1, scaledConfigurationValue: settings.param21.toInteger()).format()
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
	if(settings.param60 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 60, size: 1, scaledConfigurationValue: settings.param60.toInteger()).format()
	}
	if(settings.param61 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 61, size: 1, scaledConfigurationValue: settings.param61.toInteger()).format()
	}
	if(settings.param65 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 65, size: 2, scaledConfigurationValue: settings.param65.toInteger()).format()
	}
	if(settings.param66 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 66, size: 2, scaledConfigurationValue: settings.param66.toInteger()).format()
	}
	if(settings.param67 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 67, size: 1, scaledConfigurationValue: settings.param67.toInteger()).format()
	}
	if(settings.param68 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 68, size: 1, scaledConfigurationValue: settings.param68.toInteger()).format()
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
	log.debug "Qubino DIN Dimmer: Parsing '${description}'"
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
	log.debug "Qubino DIN Dimmer: SensorMultilevelReport handler fired"
	def resultEvents = []
	resultEvents << createEvent(name:"temperature", value: convertDegrees(location.temperatureScale,cmd), unit:"°"+location.temperatureScale, descriptionText: "Temperature: "+convertDegrees(location.temperatureScale,cmd)+"°"+location.temperatureScale)
	return resultEvents
}
/**
 * Event handler for received Switch Multilevel Report frames.
 *
 * @param void
 * @return List of events to update the ON / OFF and analogue control elements with received values.
*/
def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv3.SwitchMultilevelReport cmd){
	log.debug "Qubino DIN Dimmer: firing switch multilevel event"
	def result = []
	result << createEvent(name:"switch", value: cmd.value ? "on" : "off")
	result << createEvent(name:"level", value: cmd.value, unit:"%", descriptionText:"${device.displayName} dimmed to ${cmd.value==255 ? 100 : cmd.value}%")
	return result
}
/**
 * Event handler for received Meter Report frames. Used for displaying W and kWh measurements.
 *
 * @param void
 * @return Power consumption event for W data or kwhConsumption event for kWh data.
*/
def zwaveEvent(physicalgraph.zwave.commands.meterv3.MeterReport cmd) {
	log.debug "Qubino DIN Dimmer: firing meter report event"
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
 * Event handler for received Switch Binary Report frames. Used for ON / OFF events.
 *
 * @param cmd Switch Binary Report received from device
 * @return Switch Event with on or off value.
*/
def zwaveEvent(physicalgraph.zwave.commands.switchbinaryv1.SwitchBinaryReport cmd) {
	log.debug "Qubino DIN Dimmer: firing switch binary report event"
    createEvent(name:"switch", value: cmd.value ? "on" : "off")
}
/**
 * Event handler for received Configuration Report frames. Used for debugging purposes. 
 *
 * @param cmd Configuration Report received from device.
 * @return void.
*/
def zwaveEvent(physicalgraph.zwave.commands.configurationv2.ConfigurationReport cmd){
	log.debug "Qubino DIN Dimmer: firing configuration report event"
	log.debug cmd.configurationValue
}
/**
 * Event handler for received Basic Report frames.
 *
 * @param cmd Basic Report received from device.
 * @return void
*/
def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd){
	log.debug "Qubino DIN Dimmer: firing basic report event"
	log.debug cmd
}
/**
 * Event handler for received MultiChannelEndPointReport commands. Used to distinguish when the device is in singlechannel or multichannel configuration. 
 *
 * @param cmd communication frame.
 * @return commands to set up a MC Lifeline association.
*/
def zwaveEvent(physicalgraph.zwave.commands.multichannelv3.MultiChannelEndPointReport cmd){
	log.debug "Qubino DIN Dimmer: firing MultiChannelEndPointReport"
	if(cmd.endPoints > 0){
		state.isMcDevice = true;
		createChildDevices();
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
	log.debug "Qubino DIN Dimmer: firing MC Encapsulation event"
	def encapsulatedCommand = cmd.encapsulatedCommand()
	if (encapsulatedCommand) {
			return zwaveEvent(encapsulatedCommand, cmd)
	}
}
/**
 * Event handler for received MC Encapsulated Sensor Multilevel Report frames.
 *
 * @param cmd communication frame, command mc encapsulated communication frame; needed to distinguish sources
 * @return List of events to update the temperature control elements with received values.
*/
def zwaveEvent(physicalgraph.zwave.commands.sensormultilevelv5.SensorMultilevelReport cmd, physicalgraph.zwave.commands.multichannelv3.MultiChannelCmdEncap command){
	log.debug "Qubino DIN Dimmer: firing MC sensor multilevel event"
	def result = []
	result << createEvent(name:"temperature", value: convertDegrees(location.temperatureScale,cmd), unit:"°"+location.temperatureScale, descriptionText: "Temperature: "+convertDegrees(location.temperatureScale,cmd)+"°"+location.temperatureScale, isStateChange: true)
	return result
}
/**
 * Event handler for received MC Encapsulated Switch Multilevel Report frames.
 *
 * @param cmd communication frame, command mc encapsulated communication frame; needed to distinguish sources
 * @return List of events to update the ON / OFF and analogue control elements with received values.
*/
def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv3.SwitchMultilevelReport cmd, physicalgraph.zwave.commands.multichannelv3.MultiChannelCmdEncap command){
	log.debug "Qubino DIN Dimmer: firing MC switch multilevel event"
	log.debug cmd
	log.debug command
	def result = []
	result << createEvent(name:"switch", value: cmd.value ? "on" : "off")
	result << createEvent(name:"level", value: cmd.value, unit:"%", descriptionText:"${device.displayName} dimmed to ${cmd.value==255 ? 100 : cmd.value}%")
	return result
}
/**
 * Event handler for received MC Encapsulated Meter Report frames.
 *
 * @param cmd communication frame, command mc encapsulated communication frame; needed to distinguish sources
 * @return List of events to update power control elements with received values.
*/
def zwaveEvent(physicalgraph.zwave.commands.meterv3.MeterReport cmd, physicalgraph.zwave.commands.multichannelv3.MultiChannelCmdEncap command){
	log.debug "Qubino DIN Dimmer: firing MC Meter Report event"
	log.debug command
	log.debug cmd
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
