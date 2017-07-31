/**
 *  Qubino RGBW Dimmer
 *	Device Handler 
 *	Version 1.0
 *  Date: 31.7.2017
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
 * |---------------------------- DEVICE HANDLER FOR QUBINO RGBW DIMMER Z-WAVE DEVICE -------------------------------------------------------|  
 *		This hanlder supports all features of the device (both normal and 4 Dimmer operating modes).
 * |-----------------------------------------------------------------------------------------------------------------------------------------------|
 *
 *
 *	TO-DO:
 *
 *	CHANGELOG:
 *
 */
metadata {
	definition (name: "Qubino RGBW Dimmer", namespace: "Goap", author: "Kristjan Jam&scaron;ek") {
		capability "Actuator"
		capability "Switch"
		capability "Switch Level"
		capability "Color Control"
		
		capability "Light"			// - Tagging capability

		capability "Configuration" //Needed for configure() function to set any specific configurations
		command "setConfiguration" //command to issue Configuration Set commands to the module according to user preferences
		
		attribute "dimmerEp1", "string"
		attribute "switchEp1", "enum", ["on", "off"]
		attribute "levelEp1", "number"
		command "dimmerEp1Level"
		command "switchEp1On"
		command "switchEp1Off"
		
		attribute "dimmerEp2", "string"
		attribute "switchEp2", "enum", ["on", "off"]
		attribute "levelEp2", "number"
		command "dimmerEp2Level"
		command "switchEp2On"
		command "switchEp2Off"
		
		attribute "dimmerEp3", "string"
		attribute "switchEp3", "enum", ["on", "off"]
		attribute "levelEp3", "number"
		command "dimmerEp3Level"
		command "switchEp3On"
		command "switchEp3Off"
		
		attribute "dimmerEp4", "string"
		attribute "switchEp4", "enum", ["on", "off"]
		attribute "levelEp4", "number"
		command "dimmerEp4Level"
		command "switchEp4On"
		command "switchEp4Off"
		
		attribute "padLeft", "string"
		attribute "padRight", "string"
		
		//DEBUG

		//DEBUG END
		
        fingerprint mfr:"0159", prod:"0001", model:"0054"  //Manufacturer Information value for Qubino RGBW Dimmer
	}


	simulator {
		// TESTED WITH PHYSICAL DEVICE - UNNEEDED
	}
	tiles(scale: 2) {
		multiAttributeTile(name:"switch", type: "lighting", width: 6, height: 4, canChangeIcon: true){
			tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
				attributeState "on", label:'${name}', action:"switch.off", icon:"st.switches.light.on", backgroundColor:"#00a0dc", nextState:"turningOff"
				attributeState "off", label:'${name}', action:"switch.on", icon:"st.switches.light.off", backgroundColor:"#ffffff", nextState:"turningOn"
				attributeState "turningOn", label:'${name}', action:"switch.off", icon:"st.switches.light.on", backgroundColor:"#e6f8ff", nextState:"turningOff"
				attributeState "turningOff", label:'${name}', action:"switch.on", icon:"st.switches.light.off", backgroundColor:"#ffffff", nextState:"turningOn"
			}
			tileAttribute ("device.level", key: "SLIDER_CONTROL") {
				attributeState "level", action:"switch level.setLevel"
			}
			tileAttribute ("device.color", key: "COLOR_CONTROL") {
				attributeState "color", action:"color control.setColor"
			}
	    }
		standardTile("switchEp1", "device.switchEp1", height: 1, width: 1, inactiveLabel: false, canChangeIcon: false) {
            state "off", label:"R", action:"switchEp1On", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8", nextState:"on"
            state "on", label:"R", action:"switchEp1Off", icon:"st.illuminance.illuminance.bright", backgroundColor:"#FF0000", nextState:"off"
        }
        controlTile("dimmerEp1", "device.dimmerEp1", "slider", range:"(0..99)", height: 1, width: 4, inactiveLabel: false) {
            state "dimmerEp1", action:"dimmerEp1Level"
        }
        valueTile("levelEp1", "device.levelEp1", decoration: "flat", height: 1, width: 1) {
            state "levelEp1", label:'${currentValue}%'
        }
		standardTile("switchEp2", "device.switchEp2", height: 1, width: 1, inactiveLabel: false, canChangeIcon: false) {
            state "off", label:"G", action:"switchEp2On", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8", nextState:"on"
            state "on", label:"G", action:"switchEp2Off", icon:"st.illuminance.illuminance.bright", backgroundColor:"#00FF00", nextState:"off"
        }
        controlTile("dimmerEp2", "device.dimmerEp2", "slider", range:"(0..99)", height: 1, width: 4, inactiveLabel: false) {
            state "dimmerEp2", action:"dimmerEp2Level"
        }
        valueTile("levelEp2", "device.levelEp2", decoration: "flat", height: 1, width: 1) {
            state "levelEp2", label:'${currentValue}%'
        }
		standardTile("switchEp3", "device.switchEp3", height: 1, width: 1, inactiveLabel: false, canChangeIcon: false) {
            state "off", label:"B", action:"switchEp3On", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8", nextState:"on"
            state "on", label:"B", action:"switchEp3Off", icon:"st.illuminance.illuminance.bright", backgroundColor:"#0000FF", nextState:"off"
        }
        controlTile("dimmerEp3", "device.dimmerEp3", "slider", range:"(0..99)", height: 1, width: 4, inactiveLabel: false){
            state "dimmerEp3", action:"dimmerEp3Level"
        }
        valueTile("levelEp3", "device.levelEp3", decoration: "flat", height: 1, width: 1) {
            state "levelEp3", label:'${currentValue}%'
        }
		standardTile("switchEp4", "device.switchEp4", height: 1, width: 1, inactiveLabel: false, canChangeIcon: false) {
            state "off", label:"W", action:"switchEp4On", icon:"st.illuminance.illuminance.dark", backgroundColor:"#D8D8D8", nextState:"on"
            state "on", label:"W", action:"switchEp4Off", icon:"st.illuminance.illuminance.bright", backgroundColor:"#FFFFFF", nextState:"off"
        }
        controlTile("dimmerEp4", "device.dimmerEp4", "slider", range:"(0..99)", height: 1, width: 4, inactiveLabel: false) {
            state "dimmerEp4", action:"dimmerEp4Level"
        }
        valueTile("levelEp4", "device.levelEp4", decoration: "flat", height: 1, width: 1) {
            state "levelEp4", label:'${currentValue}%'
        }
		valueTile("padLeft", "device.padLeft", decoration: "flat", height: 1, width: 1) {
            state "padLeft", label:''
        }
		standardTile("setConfiguration", "device.setConfiguration", decoration: "flat", width: 4) {
			state("setConfiguration", label:'Set Configuration', action:'setConfiguration')
		}
		valueTile("padRight", "device.padRight", decoration: "flat", height: 1, width: 1) {
            state "padRight", label:''
        }
		main("switch")
		details(["switch", "switchEp1", "dimmerEp1", "levelEp1", "switchEp2", "dimmerEp2", "levelEp2", "switchEp3", "dimmerEp3", "levelEp3", "switchEp4", "dimmerEp4", "levelEp4", "padLeft", "setConfiguration", "padRight"])
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
					options: ["1" : "1 – NORMAL mode – momentary switch type",
							  "2" : "2 – NORMAL mode – toggle switch type",
							  "3" : "3 – NORMAL mode – toggle with memory switch type",
							  "4" : "4 – BRIGHTNESS mode – momentary switch type",
							  "5" : "5 – BRIGHTNESS mode – toggle switch type",
							  "6" : "6 – BRIGHTNESS mode – toggle with memory switch type",
							  "7" : "7 - RAINBOW mode – momentary switch type",
							  "8" : "8 – SCENE mode – momentary switch type",
							  "9" : "9 – SCENE mode – toggle switch type",
							  "10" : "10 – SCENE mode – toggle with memory switch type"],
					title: "1. Input IN1 configuration.\n " +
						   "This parameter defines the IN1 input type and function.\n" +
						   "Default value: 4."
				
				input name: "param2", type: "enum", required: false,
					options: ["1" : "1 – NORMAL mode – momentary switch type",
							  "2" : "2 – NORMAL mode – toggle switch type",
							  "3" : "3 – NORMAL mode – toggle with memory switch type",
							  "4" : "4 – BRIGHTNESS mode – momentary switch type",
							  "5" : "5 – BRIGHTNESS mode – toggle switch type",
							  "6" : "6 – BRIGHTNESS mode – toggle with memory switch type",
							  "7" : "7 - RAINBOW mode – momentary switch type",
							  "8" : "8 – SCENE mode – momentary switch type",
							  "9" : "9 – SCENE mode – toggle switch type",
							  "10" : "10 – SCENE mode – toggle with memory switch type"],
					title: "2. Input IN2 configuration.\n " +
						   "This parameter defines the IN2 input type and function.\n" +
						   "Default value: 7."
				
				input name: "param3", type: "enum", required: false,
					options: ["1" : "1 – NORMAL mode – momentary switch type",
							  "2" : "2 – NORMAL mode – toggle switch type",
							  "3" : "3 – NORMAL mode – toggle with memory switch type",
							  "4" : "4 – BRIGHTNESS mode – momentary switch type",
							  "5" : "5 – BRIGHTNESS mode – toggle switch type",
							  "6" : "6 – BRIGHTNESS mode – toggle with memory switch type",
							  "7" : "7 - RAINBOW mode – momentary switch type",
							  "8" : "8 – SCENE mode – momentary switch type",
							  "9" : "9 – SCENE mode – toggle switch type",
							  "10" : "10 – SCENE mode – toggle with memory switch type"],
					title: "3. Input IN3 configuration.\n " +
						   "This parameter defines the IN3 input type and function.\n" +
						   "Default value: 8."
				
				input name: "param4", type: "enum", required: false,
					options: ["1" : "1 – NORMAL mode – momentary switch type",
							  "2" : "2 – NORMAL mode – toggle switch type",
							  "3" : "3 – NORMAL mode – toggle with memory switch type",
							  "4" : "4 – BRIGHTNESS mode – momentary switch type",
							  "5" : "5 – BRIGHTNESS mode – toggle switch type",
							  "6" : "6 – BRIGHTNESS mode – toggle with memory switch type",
							  "7" : "7 - RAINBOW mode – momentary switch type",
							  "8" : "8 – SCENE mode – momentary switch type",
							  "9" : "9 – SCENE mode – toggle switch type",
							  "10" : "10 – SCENE mode – toggle with memory switch type"],
					title: "4. Input IN4 configuration.\n " +
						   "This parameter defines the IN4 input type and function.\n" +
						   "Default value: 1."
						   
				input name: "param5", type: "enum", required: false,
					options: ["1" : "1 – Ocean (soft flowing between shades of blue color)",
							  "2" : "2 – Lightning (fast flashing of white color)",
							  "3" : "3 – Rainbow (flowing between colors of rainbow)",
							  "4" : "4 – Snow (flowing between shades of white and cyan color)",
							  "5" : "5 – Romantic (soft flowing of the red color)",
							  "6" : "6 – Party scene (random flashing between colors)"],
					title: "5. Auto Scene Mode Set.\n " +
						   "This parameter defines the behaviour of the output in scene mode.\n" +
						   "Default value: 1."
				
				input name: "param6", type: "number", range: "1..1127", required: false,
					title: "6. Auto Scene Mode – Duration between Color change.\n " +
							"This parameter is used to adjust time between 2 Colours in the Scene.\n" +
							"Available settings:\n" +
							"1-127 – delay duration is 1 sec to 127 sec.\n" +
							"1001-1127 – delay duration is from 1 min to 127 min. This parameter has no effect on Lighting and Party Scene.\n" +
							"Default value: 3."
							
				input name: "param7", type: "enum", required: false,
					options: ["0" : "0 – device does not memorize its status at power cut. Load is disconnected.",
							  "1" : "1 – device memorizes its status at the power cut. Load will be set to the status from before powercut."]
					title: "7. Memorize device status at power cut.\n" +
							"Device will be set to status memorized before power cut.\n" +
							"Default value: 0."

				input name: "param8", type: "number", range: "0..32536", required: false,
					title: "8. Automatic turning off output after set time.\n" +
						   "Output is turned automatically off after the time, set in this parameter.\n" +
						   "Available settings:\n" +
							"0 – Auto OFF disabled.\n" +
							"1 – 32536 = 1 second – 32536 seconds Auto OFF.\n" +
							"Default value: 0."
							
				input name: "param9", type: "number", range: "0..32536", required: false,
					title: "9. Automatic turning on output after set time.\n" +
						   "Output is turned automatically on after the time, set in this parameter.\n" +
						   "Available settings:\n" +
							"0 – Auto ON disabled.\n" +
							"1 – 32536 = 1 second – 32536 seconds Auto ON.\n" +
							"Default value: 0."
							
				input name: "param10", type: "number", range: "2..99", required: false,
					title: "10. MAX dimming value.\n" +
						   "Available settings:\n" +
							"2-99 = 2 % – 99 %.\n" +
							"Default value: 99."

				input name: "param11", type: "number", range: "1..98", required: false,
					title: "11. MIN dimming value.\n" +
						   "Available settings:\n" +
							"1-98 = 1 % – 98 %.\n" +
							"Default value: 1.\n" + 
							"NOTE: The minimum level may not be higher than the MAX dimming value."

				input name: "param12", type: "number", range: "5..25", required: false,
					title: "12. Dimming time (soft on/off).\n" +
						   "Available settings:\n" +
							"5 – 25 = from 0.5 to 2.5 seconds.\n" +
							"Default value: 10 = 1s"

				input name: "param13", type: "number", range: "1..127", required: false,
					title: "13. Dimming time when key pressed.\n" +
						   "Available settings:\n" +
							"1 – 127 = from 1 to 127 seconds.\n" +
							"Default value: 3 = 3s\n" +
							"NOTE: Dimming time depends also on Min and Max dimming value."

				input name: "param14", type: "enum", required: false,
					options: ["0" : "0 – 4 dimmers mode disabled",
							  "1" : "1 – 4 dimmers mode enabled – momentary switch type",
							  "2" : "2 – 4 dimmers mode enabled – toggle switch type",
							  "3" : "3 – 4 dimmers mode enabled – toggle with memory switch type"],
					title: "14. 4 Dimmers mode.\n" +
						   "Default value: 0\n" +
						   "NOTE: If the parameter no. 14 is enabled, parameter no. 1,2,3,4 has no effect. A reinclusion of the device is necessary after toggling this parameter to 1-3."
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
def rgbToHSV(red, green, blue) {
	float r = red / 255f
	float g = green / 255f
	float b = blue / 255f
	float max = [r, g, b].max()
	float delta = max - [r, g, b].min()
	def hue = 13
	def saturation = 0
	if (max && delta) {
		saturation = 100 * delta / max
		if (r == max) {
			hue = ((g - b) / delta) * 100 / 6
		} else if (g == max) {
			hue = (2 + (b - r) / delta) * 100 / 6
		} else {
			hue = (4 + (r - g) / delta) * 100 / 6
		}
	}
	return [hue: hue, saturation: saturation, value: max * 100];
}
def huesatToRGB(float hue, float sat) {
	while(hue >= 100) hue -= 100
	int h = (int)(hue / 100 * 6)
	float f = hue / 100 * 6 - h
	int p = Math.round(255 * (1 - (sat / 100)))
	int q = Math.round(255 * (1 - (sat / 100) * f))
	int t = Math.round(255 * (1 - (sat / 100) * (1 - f)))
	switch (h) {
		case 0: return [255, t, p]
		case 1: return [q, 255, p]
		case 2: return [p, 255, t]
		case 3: return [p, q, 255]
		case 4: return [t, p, 255]
		case 5: return [255, p, q]
	}
}
/**
 *  hex(value, width=2)
 *
 *  Formats an int as a hex string.
 **/
private hex(value, width=2) {
    def s = new BigInteger(Math.round(value).toString()).toString(16)
    while (s.size() < width) { s = "0" + s }
    return s
}
//RGBW FUNCTIONS
/**
 *  rgbToRGBW(colorMap)
 *
 *  Adds white key to a colorMap containing red, green, and blue keys.
 *  For now, the white value is calculated as min(red,green,blue).
 *
 *  A more-complicated translation is discussed here:
 *   http://stackoverflow.com/questions/21117842/converting-an-rgbw-color-to-a-standard-rgb-hsb-rappresentation
 *  But for now we're keeping it simple.
 **/
private rgbToRGBW(Map colorMap) {
    if (state.debug) log.trace "${device.displayName}: rgbToRGBW(): Translating colorMap: ${colorMap}"

    if (colorMap.containsKey("red") & colorMap.containsKey("green") & colorMap.containsKey("blue")) {
        def w = [colorMap.red, colorMap.green, colorMap.blue].min()
        return colorMap << [ white: w ]
    }
    else {
        log.error "${device.displayName}: rgbToRGBW(): Cannot obtain color information from colorMap: ${colorMap}"
    }
}
/**
 *  hsvToRGBW(colorMap)
 *
 *  Adds red, green, blue, and white keys to a colorMap containing hue, saturation, level (value) keys.
 **/
private hsvToRGBW(Map colorMap) {
    if (colorMap.containsKey("hue") & colorMap.containsKey("saturation") & colorMap.containsKey("level")) {
        float h = colorMap.hue / 100
        while (h >= 1) h -= 1
        float s = colorMap.saturation / 100
        float v = colorMap.level * 255 / 100

        int d = (int) h * 6
        float f = (h * 6) - d
        int n = Math.round(v)
        int p = Math.round(v * (1 - s))
        int q = Math.round(v * (1 - f * s))
        int t = Math.round(v * (1 - (1 - f) * s))
		log.debug d
        switch (d) {
          case 0: return colorMap << [ red: n, green: t, blue: p, white: [n,t,p].min() ]
          case 1: return colorMap << [ red: q, green: n, blue: p, white: [q,n,p].min() ]
          case 2: return colorMap << [ red: p, green: n, blue: t, white: [p,n,t].min() ]
          case 3: return colorMap << [ red: p, green: q, blue: n, white: [p,q,n].min() ]
          case 4: return colorMap << [ red: t, green: p, blue: n, white: [t,p,n].min() ]
          case 5: return colorMap << [ red: n, green: p, blue: q, white: [n,p,q].min() ]
        }
    }
    else {
        log.error "${device.displayName}: hsvToRGBW(): Cannot obtain color information from colorMap: ${colorMap}"
    }
}
/**
*
*	scaleVarToSlider(val)
*
**/
def scaleVarToSlider(val){
	if(val == 0) return 0;
	def returnVal = (99/255) * (val - 255) + 99
	return returnVal.toInteger();
}
def scaleVarFromSlider(val){
	if(val == 0) return 0;
	def returnVal = (255/99) * (val - 99) + 255
	return returnVal.toInteger();
}
/*
*	--------	HANDLE COMMANDS SECTION	--------
*/
def dimmerEp1Level(params){
	def result = []
	if(!state.isMcDevice){
		result << zwave.switchColorV3.switchColorSet(red:scaleVarFromSlider(params)).format()
		result << zwave.switchColorV3.switchColorGet(colorComponent: "red").format()
	}else{
		result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1).encapsulate(zwave.switchMultilevelV3.switchMultilevelSet(value: params)).format()
		result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1).encapsulate(zwave.switchMultilevelV3.switchMultilevelGet()).format()
		result << zwave.switchMultilevelV3.switchMultilevelGet().format()
	}
	return response(delayBetween(result, 1500))
}
def switchEp1On(){
	def result = []
	if(!state.isMcDevice){
		result << zwave.switchColorV3.switchColorSet(red:255).format()
		result << zwave.switchColorV3.switchColorGet(colorComponent: "red").format()
	}else{
		result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1).encapsulate(zwave.switchMultilevelV3.switchMultilevelSet(value: 0x63)).format()
		result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1).encapsulate(zwave.switchMultilevelV3.switchMultilevelGet()).format()
		result << zwave.switchMultilevelV3.switchMultilevelGet().format()
	}
	
	return response(delayBetween(result, 1500))
}
def switchEp1Off(){
	def result = []
	if(!state.isMcDevice){
		result << zwave.switchColorV3.switchColorSet(red:0).format()
		result << zwave.switchColorV3.switchColorGet(colorComponent: "red").format()
	}else{
		result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1).encapsulate(zwave.switchMultilevelV3.switchMultilevelSet(value: 0x00)).format()
		result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1).encapsulate(zwave.switchMultilevelV3.switchMultilevelGet()).format()
		result << zwave.switchMultilevelV3.switchMultilevelGet().format()
	}
	return response(delayBetween(result, 1500))
}
def dimmerEp2Level(params){
	def result = []
	if(!state.isMcDevice){
		result << zwave.switchColorV3.switchColorSet(green:scaleVarFromSlider(params)).format()
		result << zwave.switchColorV3.switchColorGet(colorComponent: "green").format()
	}else{
		result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:2).encapsulate(zwave.switchMultilevelV3.switchMultilevelSet(value: params)).format()
		result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:2).encapsulate(zwave.switchMultilevelV3.switchMultilevelGet()).format()
		result << zwave.switchMultilevelV3.switchMultilevelGet().format()
	}
	return response(delayBetween(result, 1500))
}
def switchEp2On(){
	def result = []
	if(!state.isMcDevice){
		result << zwave.switchColorV3.switchColorSet(green:255).format()
		result << zwave.switchColorV3.switchColorGet(colorComponent: "green").format()
	}else{
		result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:2).encapsulate(zwave.switchMultilevelV3.switchMultilevelSet(value: 0x63)).format()
		result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:2).encapsulate(zwave.switchMultilevelV3.switchMultilevelGet()).format()
		result << zwave.switchMultilevelV3.switchMultilevelGet().format()
	}
	
	return response(delayBetween(result, 1500))
}
def switchEp2Off(){
	def result = []
	if(!state.isMcDevice){
		result << zwave.switchColorV3.switchColorSet(green:0).format()
		result << zwave.switchColorV3.switchColorGet(colorComponent: "green").format()
	}else{
		result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:2).encapsulate(zwave.switchMultilevelV3.switchMultilevelSet(value: 0x00)).format()
		result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:2).encapsulate(zwave.switchMultilevelV3.switchMultilevelGet()).format()
		result << zwave.switchMultilevelV3.switchMultilevelGet().format()
	}
	return response(delayBetween(result, 1500))
}
def dimmerEp3Level(params){
	def result = []
	if(!state.isMcDevice){
		result << zwave.switchColorV3.switchColorSet(blue:scaleVarFromSlider(params)).format()
		result << zwave.switchColorV3.switchColorGet(colorComponent: "blue").format()
	}else{
		result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:3).encapsulate(zwave.switchMultilevelV3.switchMultilevelSet(value: params)).format()
		result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:3).encapsulate(zwave.switchMultilevelV3.switchMultilevelGet()).format()
		result << zwave.switchMultilevelV3.switchMultilevelGet().format()
	}
	return response(delayBetween(result, 1500))
}
def switchEp3On(){
	def result = []
	if(!state.isMcDevice){
		result << zwave.switchColorV3.switchColorSet(blue:255).format()
		result << zwave.switchColorV3.switchColorGet(colorComponent: "blue").format()
	}else{
		result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:3).encapsulate(zwave.switchMultilevelV3.switchMultilevelSet(value: 0x63)).format()
		result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:3).encapsulate(zwave.switchMultilevelV3.switchMultilevelGet()).format()
		result << zwave.switchMultilevelV3.switchMultilevelGet().format()
	}
	return response(delayBetween(result, 1500))
}
def switchEp3Off(){
	def result = []
	if(!state.isMcDevice){
		result << zwave.switchColorV3.switchColorSet(blue:0).format()
		result << zwave.switchColorV3.switchColorGet(colorComponent: "blue").format()
	}else{
		result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:3).encapsulate(zwave.switchMultilevelV3.switchMultilevelSet(value: 0x00)).format()
		result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:3).encapsulate(zwave.switchMultilevelV3.switchMultilevelGet()).format()
		result << zwave.switchMultilevelV3.switchMultilevelGet().format()
	}
	return response(delayBetween(result, 1500))
}
def dimmerEp4Level(params){
	def result = []
	if(!state.isMcDevice){
		result << zwave.switchColorV3.switchColorSet(warmWhite:scaleVarFromSlider(params)).format()
		result << zwave.switchColorV3.switchColorGet(colorComponent: "warmWhite").format()
	}else{
		result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:4).encapsulate(zwave.switchMultilevelV3.switchMultilevelSet(value: params)).format()
		result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:4).encapsulate(zwave.switchMultilevelV3.switchMultilevelGet()).format()
		result << zwave.switchMultilevelV3.switchMultilevelGet().format()
	}
	return response(delayBetween(result, 1500))
}
def switchEp4On(){
	def result = []
	if(!state.isMcDevice){
		result << zwave.switchColorV3.switchColorSet(warmWhite:255).format()
		result << zwave.switchColorV3.switchColorGet(colorComponent: "warmWhite").format()
	}else{
		result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:4).encapsulate(zwave.switchMultilevelV3.switchMultilevelSet(value: 0x63)).format()
		result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:4).encapsulate(zwave.switchMultilevelV3.switchMultilevelGet()).format()
		result << zwave.switchMultilevelV3.switchMultilevelGet().format()
	}
	return response(delayBetween(result, 1500))
}
def switchEp4Off(){
	def result = []
	if(!state.isMcDevice){
		result << zwave.switchColorV3.switchColorSet(warmWhite:0).format()
		result << zwave.switchColorV3.switchColorGet(colorComponent: "warmWhite").format()
	}else{
		result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:4).encapsulate(zwave.switchMultilevelV3.switchMultilevelSet(value: 0x00)).format()
		result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:4).encapsulate(zwave.switchMultilevelV3.switchMultilevelGet()).format()
		result << zwave.switchMultilevelV3.switchMultilevelGet().format()
	}
	return response(delayBetween(result, 1500))
}
/**
 * Color Control capability command handler.
 *
 * @param void
 * @return List of commands that will be executed in sequence with 500 ms delay inbetween.
*/
def setColor(value) {
	log.debug "Qubino RGBW Dimmer: setColor()"
	def result = []
	if(state.isMcDevice){
		if (value.hex) {
			def c = value.hex.findAll(/[0-9a-fA-F]{2}/).collect { Integer.parseInt(it, 16) }
			def r = scaleVarToSlider(c[0])
			def g = scaleVarToSlider(c[1])
			def b = scaleVarToSlider(c[2])
			def w = [r,g,b].min()
			result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1).encapsulate(zwave.switchMultilevelV3.switchMultilevelSet(value: r)).format()
			result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:2).encapsulate(zwave.switchMultilevelV3.switchMultilevelSet(value: g)).format()
			result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:3).encapsulate(zwave.switchMultilevelV3.switchMultilevelSet(value: b)).format()
			result << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:4).encapsulate(zwave.switchMultilevelV3.switchMultilevelSet(value: w)).format()
		}
	}else{
		if (value.hex) {
			def c = value.hex.findAll(/[0-9a-fA-F]{2}/).collect { Integer.parseInt(it, 16) }
			def w = [c[0],c[1],c[2]].min()
			result << zwave.switchColorV3.switchColorSet(red:c[0], green:c[1], blue:c[2], warmWhite:w).format()
			result << zwave.switchColorV3.switchColorGet(colorComponent: "warmWhite").format()
			result << zwave.switchColorV3.switchColorGet(colorComponent: "red").format()
			result << zwave.switchColorV3.switchColorGet(colorComponent: "green").format()
			result << zwave.switchColorV3.switchColorGet(colorComponent: "blue").format()
		} else {
			def hue = value.hue ?: device.currentValue("hue")
			def saturation = value.saturation ?: device.currentValue("saturation")
			if(hue == null) hue = 13
			if(saturation == null) saturation = 13
			def rgb = huesatToRGB(hue, saturation)
			def w = [rgb[0],rgb[1],rgb[2]].min()
			result << zwave.switchColorV3.switchColorSet(red: rgb[0], green: rgb[1], blue: rgb[2], warmWhite:w).format()
			result << zwave.switchColorV3.switchColorGet(colorComponent: "warmWhite").format()
			result << zwave.switchColorV3.switchColorGet(colorComponent: "red").format()
			result << zwave.switchColorV3.switchColorGet(colorComponent: "green").format()
			result << zwave.switchColorV3.switchColorGet(colorComponent: "blue").format()
		}
	}
	if(value.hue != null) sendEvent(name: "hue", value: value.hue)
	if(value.hex) sendEvent(name: "color", value: value.hex)
	if(value.saturation != null) sendEvent(name: "saturation", value: value.saturation)
	
	if(value.red != null){
		sendEvent(name: "dimmerEp1", value: scaleVarToSlider(value.red))
		sendEvent(name: "levelEp1", value: scaleVarToSlider(value.red))
		sendEvent(name: "switchEp1", value: value.red ? "on" : "off")
	}
	if(value.green != null){
		sendEvent(name: "dimmerEp2", value: scaleVarToSlider(value.green))
		sendEvent(name: "levelEp2", value: scaleVarToSlider(value.green))
		sendEvent(name: "switchEp2", value: value.green ? "on" : "off")
	}
	if(value.blue != null){
		sendEvent(name: "dimmerEp3", value: scaleVarToSlider(value.blue))
		sendEvent(name: "levelEp3", value: scaleVarToSlider(value.blue))
		sendEvent(name: "switchEp3", value: value.blue ? "on" : "off")
	}
	def lastColor = device.latestValue("color")
	def lastRGB = lastColor.findAll(/[0-9a-fA-F]{2}/).collect { Integer.parseInt(it, 16) }
	def lastWhite = [lastRGB[0], lastRGB[1], lastRGB[2]].min()
	if(lastWhite != null){
		sendEvent(name: "dimmerEp4", value: scaleVarToSlider(lastWhite))
		sendEvent(name: "levelEp4", value: scaleVarToSlider(lastWhite))
		sendEvent(name: "switchEp4", value: lastWhite ? "on" : "off")
	}
	
	result << zwave.switchMultilevelV3.switchMultilevelGet().format()
	return response(delayBetween(result, 1000))
}

/**
 * Configuration capability command handler.
 *
 * @param void
 * @return List of commands that will be executed in sequence with 500 ms delay inbetween.
*/
def configure() {
	log.debug "Qubino Flush Dimmer: configure()"
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
 * setConfigurationParams command handler that sets user selected configuration parameters on the device. 
 * In case no value is set for a specific parameter the method skips setting that parameter.
 *
 * @param void
 * @return List of Configuration Set commands that will be executed in sequence with 500 ms delay inbetween.
*/

def setConfiguration() {
	log.debug "Qubino Flush Dimmer: setConfiguration()"
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
	if(settings.param4 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 4, size: 1, scaledConfigurationValue: settings.param4.toInteger()).format()
	}
	if(settings.param5 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 5, size: 1, scaledConfigurationValue: settings.param5.toInteger()).format()
	}
	if(settings.param6 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 6, size: 2, scaledConfigurationValue: settings.param6.toInteger()).format()
	}
	if(settings.param7 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 7, size: 1, scaledConfigurationValue: settings.param7.toInteger()).format()
	}
	if(settings.param8 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 8, size: 2, scaledConfigurationValue: settings.param8.toInteger()).format()
	}
	if(settings.param9 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 9, size: 2, scaledConfigurationValue: settings.param9.toInteger()).format()
	}
	if(settings.param10 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 10, size: 1, scaledConfigurationValue: settings.param10.toInteger()).format()
	}
	if(settings.param11 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 11, size: 1, scaledConfigurationValue: settings.param11.toInteger()).format()
	}
	if(settings.param12 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 12, size: 1, scaledConfigurationValue: settings.param12.toInteger()).format()
	}
	if(settings.param13 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 13, size: 1, scaledConfigurationValue: settings.param13.toInteger()).format()
	}
	if(settings.param14 != null){
		configSequence << zwave.configurationV1.configurationSet(parameterNumber: 14, size: 1, scaledConfigurationValue: settings.param14.toInteger()).format()
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
	log.debug "Qubino RGBW Dimmer: Parsing '${description}'"
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
 * Event handler for received Switch Multilevel Report frames.
 *
 * @param void
 * @return List of events to update the ON / OFF and analogue control elements with received values.
*/
def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv3.SwitchMultilevelReport cmd){
	log.debug "Qubino RGBW Dimmer: firing switch multilevel event"
	def result = []
	result << createEvent(name:"switch", value: cmd.value ? "on" : "off")
	result << createEvent(name:"level", value: cmd.value, unit:"%", descriptionText:"${device.displayName} dimmed to ${cmd.value==255 ? 100 : cmd.value}%")
	return result
}

/**
 * Event handler for received Switch Binary Report frames. Used for ON / OFF events.
 *
 * @param void
 * @return Switch Event with on or off value.
*/
def zwaveEvent(physicalgraph.zwave.commands.switchbinaryv1.SwitchBinaryReport cmd) {
	log.debug "Qubino RGBW Dimmer: firing switch binary report event"
    createEvent(name:"switch", value: cmd.value ? "on" : "off")
}
/**
 * Event handler for received Configuration Report frames. Used for debugging purposes. 
 *
 * @param void
 * @return void.
*/
def zwaveEvent(physicalgraph.zwave.commands.configurationv2.ConfigurationReport cmd){
	log.debug "Qubino RGBW Dimmer: firing configuration report event"
}
/**
 * Event handler for received MultiChannelEndPointReport commands. Used to distinguish when the device is in singlechannel or multichannel configuration. 
 *
 * @param cmd communication frame
 * @return commands to set up a MC Lifeline association.
*/
def zwaveEvent(physicalgraph.zwave.commands.multichannelv3.MultiChannelEndPointReport cmd){
	log.debug "Qubino RGBW Dimmer: firing MultiChannelEndPointReport"
	def cmds = []
	if(cmd.endPoints > 0 && !state.isMcDevice){
		state.isMcDevice = true;
		cmds << response(zwave.associationV1.associationRemove(groupingIdentifier:1).format())
		cmds << response(zwave.multiChannelAssociationV2.multiChannelAssociationSet(groupingIdentifier: 1, nodeId: [0,zwaveHubNodeId,1]).format())
	}
	return cmds
}
/**
 * Event handler for received Multi Channel Encapsulated commands.
 *
 * @param cmd encapsulated communication frame
 * @return parsed event.
*/
def zwaveEvent(physicalgraph.zwave.commands.multichannelv3.MultiChannelCmdEncap cmd){
	log.debug "Qubino RGBW Dimmer: firing MC Encapsulation event"
	def encapsulatedCommand = cmd.encapsulatedCommand()
	//log.debug ("Command from endpoint ${cmd.sourceEndPoint}: ${encapsulatedCommand}")
	if (encapsulatedCommand) {
			return zwaveEvent(encapsulatedCommand, cmd)
	}
}
/**
 * Event handler for received MC Encapsulated Switch Multilevel Report frames.
 *
 * @param cmd communication frame, command mc encapsulated communication frame; needed to distinguish sources
 * @return List of events to update the ON / OFF and analogue control elements with received values.
*/

def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv3.SwitchMultilevelReport cmd, physicalgraph.zwave.commands.multichannelv3.MultiChannelCmdEncap command){
	log.debug "Qubino RBGW Dimmer: firing MC switch multilevel event"
	def result = []
	switch(command.sourceEndPoint){
		default: 
			break;
		case 1:
			def r = scaleVarFromSlider(cmd.value.toInteger())
			def g = scaleVarFromSlider(device.latestValue("dimmerEp2").toInteger())
			def b = scaleVarFromSlider(device.latestValue("dimmerEp3").toInteger())
			def w = scaleVarFromSlider(device.latestValue("dimmerEp4").toInteger())
			def hueSat = rgbToHSV(r, g, b)
			sendEvent(name: "color", value: '#'+hex(r).toUpperCase()+""+hex(g).toUpperCase()+""+hex(b).toUpperCase())
			sendEvent(name: "hue", value: hueSat.hue)
			sendEvent(name: "saturation", value: hueSat.saturation)
			sendEvent(name: "dimmerEp1", value: cmd.value)
			sendEvent(name: "levelEp1", value: cmd.value)
			sendEvent(name: "switchEp1", value: cmd.value ? "on" : "off")
			result << response(zwave.switchMultilevelV1.switchMultilevelGet().format())
			break;
		case 2:
			def r = scaleVarFromSlider(device.latestValue("dimmerEp1").toInteger())
			def g = scaleVarFromSlider(cmd.value.toInteger())
			def b = scaleVarFromSlider(device.latestValue("dimmerEp3").toInteger())
			def w = scaleVarFromSlider(device.latestValue("dimmerEp4").toInteger())
			def hueSat = rgbToHSV(r, g, b)
			sendEvent(name: "color", value: '#'+hex(r).toUpperCase()+""+hex(g).toUpperCase()+""+hex(b).toUpperCase())
			sendEvent(name: "hue", value: hueSat.hue)
			sendEvent(name: "saturation", value: hueSat.saturation)
			sendEvent(name: "dimmerEp2", value: cmd.value)
			sendEvent(name: "levelEp2", value: cmd.value)
			sendEvent(name: "switchEp2", value: cmd.value ? "on" : "off")
			result << response(zwave.switchMultilevelV1.switchMultilevelGet().format())
			break;
		case 3:
			def r = scaleVarFromSlider(device.latestValue("dimmerEp1").toInteger())
			def g = scaleVarFromSlider(device.latestValue("dimmerEp2").toInteger())
			def b = scaleVarFromSlider(cmd.value.toInteger())
			def w = scaleVarFromSlider(device.latestValue("dimmerEp4").toInteger())
			def hueSat = rgbToHSV(r, g, b)
			sendEvent(name: "color", value: '#'+hex(r).toUpperCase()+""+hex(g).toUpperCase()+""+hex(b).toUpperCase())
			sendEvent(name: "hue", value: hueSat.hue)
			sendEvent(name: "saturation", value: hueSat.saturation)
			sendEvent(name: "dimmerEp3", value: cmd.value)
			sendEvent(name: "levelEp3", value: cmd.value)
			sendEvent(name: "switchEp3", value: cmd.value ? "on" : "off")
			result << response(zwave.switchMultilevelV1.switchMultilevelGet().format())
			break;
		case 4:
			def r = scaleVarFromSlider(device.latestValue("dimmerEp1").toInteger())
			def g = scaleVarFromSlider(device.latestValue("dimmerEp2").toInteger())
			def b = scaleVarFromSlider(device.latestValue("dimmerEp3").toInteger())
			def w = scaleVarFromSlider(cmd.value.toInteger())
			def hueSat = rgbToHSV(r, g, b)
			sendEvent(name: "color", value: '#'+hex(r).toUpperCase()+""+hex(g).toUpperCase()+""+hex(b).toUpperCase())
			sendEvent(name: "hue", value: hueSat.hue)
			sendEvent(name: "saturation", value: hueSat.saturation)
			sendEvent(name: "dimmerEp4", value: cmd.value)
			sendEvent(name: "levelEp4", value: cmd.value)
			sendEvent(name: "switchEp4", value: cmd.value ? "on" : "off")
			result << response(zwave.switchMultilevelV1.switchMultilevelGet().format())
			break;
	}
	return result;
}
/**
 * Event handler for received Color Switch Report frames.
 *
 * @param cmd communication frame, command mc encapsulated communication frame; needed to distinguish sources
 * @return List of events to update the ON / OFF and analogue control elements with received values.
*/
def zwaveEvent(physicalgraph.zwave.commands.switchcolorv3.SwitchColorReport cmd){
	def result = []
	if(cmd.colorComponentId != null && cmd.value != null){
		switch(cmd.colorComponentId){
			default:
			break;
			case 0:
				def rgb = huesatToRGB(device.latestValue("hue"), device.latestValue("saturation"))
				def r = hex(rgb[0],2)
				def g = hex(rgb[1],2)
				def b = hex(rgb[2],2)
				sendEvent(name: "color", value: '#'+r.toUpperCase()+""+g.toUpperCase()+""+b.toUpperCase())
				sendEvent(name: "dimmerEp4", value: scaleVarToSlider(cmd.value))
				sendEvent(name: "levelEp4", value: scaleVarToSlider(cmd.value))
				sendEvent(name:"switchEp4", value: cmd.value ? "on" : "off")
				result << response(zwave.switchMultilevelV1.switchMultilevelGet().format())
				break;
			case 2:
				def rgb = huesatToRGB(device.latestValue("hue"), device.latestValue("saturation"))
				def r = hex(cmd.value,2)
				def g = hex(rgb[1],2)
				def b = hex(rgb[2],2)
				def hueSat = rgbToHSV(cmd.value, rgb[1], rgb[2])
				sendEvent(name: "color", value: '#'+r.toUpperCase()+""+g.toUpperCase()+""+b.toUpperCase())
				sendEvent(name: "hue", value: hueSat.hue)
				sendEvent(name: "saturation", value: hueSat.saturation)
				sendEvent(name: "dimmerEp1", value: scaleVarToSlider(cmd.value))
				sendEvent(name: "levelEp1", value: scaleVarToSlider(cmd.value))
				sendEvent(name: "switchEp1", value: cmd.value ? "on" : "off")
				result << response(zwave.switchMultilevelV1.switchMultilevelGet().format())
				break;
			case 3:
				def rgb = huesatToRGB(device.latestValue("hue"), device.latestValue("saturation"))
				def r = hex(rgb[0],2)
				def g = hex(cmd.value,2)
				def b = hex(rgb[2],2)
				def hueSat = rgbToHSV(rgb[0], cmd.value, rgb[2])
				sendEvent(name: "color", value: '#'+r.toUpperCase()+""+g.toUpperCase()+""+b.toUpperCase())
				sendEvent(name: "hue", value: hueSat.hue)
				sendEvent(name: "saturation", value: hueSat.saturation)
				sendEvent(name: "dimmerEp2", value: scaleVarToSlider(cmd.value))
				sendEvent(name: "levelEp2", value: scaleVarToSlider(cmd.value))
				sendEvent(name: "switchEp2", value: cmd.value ? "on" : "off")
				result << response(zwave.switchMultilevelV1.switchMultilevelGet().format())
				break;
			case 4:
				def rgb = huesatToRGB(device.latestValue("hue"), device.latestValue("saturation"))
				def r = hex(rgb[0],2)
				def g = hex(rgb[1],2)
				def b = hex(cmd.value,2)
				def hueSat = rgbToHSV(rgb[0], rgb[1], cmd.value.toInteger())
				sendEvent(name: "color", value: '#'+r.toUpperCase()+""+g.toUpperCase()+""+b.toUpperCase())
				sendEvent(name: "hue", value: hueSat.hue)
				sendEvent(name: "saturation", value: hueSat.saturation)
				sendEvent(name: "dimmerEp3", value: scaleVarToSlider(cmd.value))
				sendEvent(name: "levelEp3", value: scaleVarToSlider(cmd.value))
				sendEvent(name: "switchEp3", value: cmd.value ? "on" : "off")
				result << response(zwave.switchMultilevelV1.switchMultilevelGet().format())
				break;
		}
	}
	return result;
}
