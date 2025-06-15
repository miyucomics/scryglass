package miyucomics.scryglass.state

interface PlayerEntityMinterface {
	fun getWindowSize(): Pair<Double, Double>
	fun setWindowSize(size: Pair<Double, Double>)
	fun getScryglassState(): ScryglassState
}