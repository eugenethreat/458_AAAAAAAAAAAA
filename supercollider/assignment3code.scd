//angry main sound
(
SynthDef.new(\freqTest, {
	//var freq, amp, sig;
	//freq = 250;

	arg freq = 250;
	var amp, sig;
	amp = 5;
	sig = SinOsc.ar(freq);
	Out.ar([0,1], sig);
}).add;
)

//-----------------------------------------------------

//low tones (background)
//https://pages.mtu.edu/~suits/notefreq432.html

(
SynthDef.new(\low1, {
	//var freq, amp, sig;
	//freq = 250;

	arg freq = 432; //a4;
	var amp, sig;
	amp = 0.5;
	sig = SinOsc.ar(freq);
	Out.ar(0, sig);
}).add;
)

(
SynthDef.new(\low2, {
	//var freq, amp, sig;
	//freq = 250;

	arg freq = 769.74; //g5
	var amp, sig;
	amp = 0.5;
	sig = SinOsc.ar(freq);
	Out.ar(1, sig);
}).add;
)

//---------------------------------------------------------
//angry low tones
//d4 =  144.16
(
SynthDef.new(\lowD, {
	//var freq, amp, sig;
	//freq = 250;

	arg freq =  288.33; //g5
	var amp, sig;
	amp = 1;
	sig = SinOsc.ar(freq);
	Out.ar(1, sig);
}).add;
)



//---------------------------------------------------------
//OSC dealings/config
~me = NetAddr.new("127.0.0.1", 57120);
NetAddr.localAddr
NetAddr.langPort;

//the ping
(
SynthDef.new(\ping, {
	arg freq = 500, pan = 0, out = 0;
	var sig, env;
	env = EnvGen.kr(Env.perc(0.01, 1), doneAction:2);
	sig = SinOsc.ar(freq, 0, 0.2!2) * env;
	sig = Pan2.ar(sig, pan);
	Out.ar(out, sig);
}).add;
)

//the osc
(
OSCdef.new(\makePing, {
	arg msg;
	Synth.new(\ping);
}, '/makePing');
)
//end of makePing

//with different port
n = NetAddr.new("127.0.0.1", 57120);
(
OSCdef.new(\makePing2, {
	arg msg;
	Synth.new(\ping);
}, '/makePing2', n, 57120);
)

thisProcess.openUDPPort(7777); // attempt to open 7777

~me = NetAddr.new("127.0.0.1", 57120);
~me2 = NetAddr.new("127.0.0.1", 57120);
~me.sendMsg('/makePing', 1);
~me2.sendMsg('/makePing2', 1);

Synth.new(\ping, [\freq, 1000, \pan, 0])
OSCdef.freeAll;

//oh my goddd
//AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
(
OSCdef.new(\makePing3, {
	arg msg;
	Synth.new(\ping);
}, '/makePing3');
)


//testing network ^^^^^^^^^

~count = 1;
//holy shit it works
(
OSCdef.new(\addOne, {
	arg msg;
	~count = ~count + 1;
	~martin.set(\freq, 500 * ~count);
}, '/addOne');
)

~martin = Synth.new(\freqTest);

thisProcess.openUDPPort(57121); // attempt to open 7777
~testFromServer = NetAddr.new("127.0.0.1", 57121);



//---------------------------------------------------------

//controls
//initial tones
(
~low1 = Synth.new(\low1);
~low2 = Synth.new(\low2);
)

//angry technology
~martin = Synth.new(\freqTest);

//count
~count = 1;
~count = ~count + 1;

(
~martin.set(\freq, 500 * ~count);
)

//reaching a fever pitch
~angry = Synth.new(\lowD);

//you are...
(
~low1.free;
~low2.free;
~martin.free;
~angry.free;
)

















