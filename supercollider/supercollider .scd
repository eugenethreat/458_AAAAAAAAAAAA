/*s.boot;
//soundin == getting live sound
(
x = {
	var sig, rmod;
	sig = SoundIn.ar([0,0]);
	rmod = sig * SinOsc.ar(1000);
	(sig + rmod) * 10;
}.play;
)
x.free;*/

//ar == audio rate
//kr == control rate
//ir ==


//https://pages.mtu.edu/~suits/notefreq432.html

//A
(
a = {
	SinOsc.ar([432,432],0,0.2);
}.play;
)


//B
(
b = {SinOsc.ar([ 484.90, 484.90],2,0.2)
}.play;
)

//D
(
d = {SinOsc.ar([ 576.65, 576.65],2,0.2)
}.play;
)

//E
(
e = {SinOsc.ar([ 647.27, 647.27],2,0.2)
}.play;
)

//E1
(
f = {SinOsc.ar([ 323, 323],2,0.2)
}.play;
)

a.free;
b.free;
d.free;
e.free;
f.free;
y.free;


//testing modulating freq/phase based on live input
(
y = {
	var sig, blah;
	blah = SoundIn.ar([0,0]);
	sig = {SinOsc.ar([ 647.27 * blah,  647.27 * blah],0, 5)};
	//sig = {SinOsc.ar([ 647.27,  647.27],0, 0.2)};

}.play;
)

(
zed = {

      }
)

s.boot;
s.free;






