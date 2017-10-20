package com.heartbeat

import net.corda.core.contracts.SchedulableState
import net.corda.core.contracts.ScheduledActivity
import net.corda.core.contracts.StateRef
import net.corda.core.flows.FlowLogicRefFactory
import net.corda.core.identity.Party
import java.time.Instant

/**
 * Every Heartbeat state has a scheduled activity to start a flow to consume itself and produce a
 * new Heartbeat state on the ledger after five seconds.
 *
 * @property me The creator of the Heartbeat state.
 * @property nextActivityTime When the scheduled activity should be kicked off.
 */
class HeartState(private val me: Party) : SchedulableState {
    override val participants get() = listOf(me)
    // A heartbeat will be emitted every second.
    // Do not use Instant.now or other methods to get the current time in nextScheduledActivity,
    // or the time will be constantly re-evaluated and always be in the future.
    private val nextActivityTime = Instant.now().plusSeconds(1)

    override fun nextScheduledActivity(thisStateRef: StateRef, flowLogicRefFactory: FlowLogicRefFactory): ScheduledActivity? {
        return ScheduledActivity(flowLogicRefFactory.create(HeartbeatFlow::class.java, thisStateRef), nextActivityTime)
    }
}