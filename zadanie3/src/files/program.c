#include "common.h"
void state_idle();
void state_active();
void state_waitingForAirConditionerAndWindow();
void state_waitingForLightAndWindow();
void state_waitingForAirConditionerAndLight();
void state_waitingForAirConditioner();
void state_waitingForLight();
void state_waitingForWindow();
void state_unlockedPanel();

void state_idle() {
	send_event('D');
	send_event('L');
	char ev;
	while (ev = read_command()) { 
		switch (ev) {
			case 'c': {
				return state_active();
			}
			case 'd': {
				return state_idle();
			}
		}
	}
}
void state_active() {
	char ev;
	while (ev = read_command()) { 
		switch (ev) {
			case 's': {
				return state_waitingForAirConditionerAndWindow();
			}
			case 'a': {
				return state_waitingForLightAndWindow();
			}
			case 'o': {
				return state_waitingForAirConditionerAndLight();
			}
			case 'd': {
				return state_idle();
			}
		}
	}
}
void state_waitingForAirConditionerAndWindow() {
	char ev;
	while (ev = read_command()) { 
		switch (ev) {
			case 'a': {
				return state_waitingForLight();
			}
			case 'o': {
				return state_waitingForAirConditioner();
			}
			case 't': {
				return state_active();
			}
			case 'd': {
				return state_idle();
			}
		}
	}
}
void state_waitingForLightAndWindow() {
	char ev;
	while (ev = read_command()) { 
		switch (ev) {
			case 's': {
				return state_waitingForWindow();
			}
			case 'o': {
				return state_waitingForLight();
			}
			case 'x': {
				return state_active();
			}
			case 'd': {
				return state_idle();
			}
		}
	}
}
void state_waitingForAirConditionerAndLight() {
	char ev;
	while (ev = read_command()) { 
		switch (ev) {
			case 's': {
				return state_waitingForAirConditioner();
			}
			case 'a': {
				return state_waitingForLight();
			}
			case 'w': {
				return state_active();
			}
			case 'd': {
				return state_idle();
			}
		}
	}
}
void state_waitingForAirConditioner() {
	char ev;
	while (ev = read_command()) { 
		switch (ev) {
			case 'a': {
				return state_unlockedPanel();
			}
			case 't': {
				return state_waitingForAirConditionerAndLight();
			}
			case 'w': {
				return state_waitingForAirConditionerAndWindow();
			}
			case 'd': {
				return state_idle();
			}
		}
	}
}
void state_waitingForLight() {
	char ev;
	while (ev = read_command()) { 
		switch (ev) {
			case 's': {
				return state_unlockedPanel();
			}
			case 'x': {
				return state_waitingForAirConditionerAndLight();
			}
			case 'w': {
				return state_waitingForLightAndWindow();
			}
			case 'd': {
				return state_idle();
			}
		}
	}
}
void state_waitingForWindow() {
	char ev;
	while (ev = read_command()) { 
		switch (ev) {
			case 'o': {
				return state_unlockedPanel();
			}
			case 't': {
				return state_waitingForLightAndWindow();
			}
			case 'x': {
				return state_waitingForAirConditionerAndWindow();
			}
			case 'd': {
				return state_idle();
			}
		}
	}
}
void state_unlockedPanel() {
	send_event('U');
	send_event('C');
	char ev;
	while (ev = read_command()) { 
		switch (ev) {
			case 'p': {
				return state_idle();
			}
			case 'd': {
				return state_idle();
			}
		}
	}
}
void main() {
	state_idle();
}