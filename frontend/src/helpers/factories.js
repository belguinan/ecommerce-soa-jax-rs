/**
 * Did item factory
 * 
 * @param  {Object} params
 * @return {Object}       
 */
export function didFactory(params = {}) {
    return {
        did: '',
        emails: '',
        tbtm_did: '',
        tbtm_direct: '',
        payload: '{}',
        ...params,
    }
}


/**
 * Did item factory
 * 
 * @param  {Object} params
 * @return {Object}       
 */
export function voiceFactory(params = {}) {
    return {
        provider: "ElevenLabs",
        label: "Rosa",
        voice: "zuri",
        similarity: "0.8",
        stability: "0.8",
        model: "eleven_turbo_v2_5",
        voice_id: "C3x1TEM7scV4p2AXJyrp",
        ...params,
    }
}