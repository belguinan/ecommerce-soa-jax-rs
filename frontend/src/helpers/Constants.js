export const STATIC_VARIABLES = [
	
	// PROMPT VARIABLES
	'{context}', 
	'{total_cost_for_this_reservation}', 
	'{conversation_history}',
	'{validation_message}',

	// 
	'{voice_name}', 
	'{customer_name}',

	// DID VARIABLES
	'{tbtm_customer_name}',
	'{tbtm_customer_company}',
	'{tbtm_customer_address}',
];

export const VARIABLES_REGEX = /\{[a-z0-9]+(?:_[a-z0-9]+)*\}/g;

export const NUMBERS_REGEX = /[^0-9]/g;
