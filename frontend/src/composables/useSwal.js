import { inject } from 'vue'
import { useFetch } from '@/composables/useFetch.js'

const { fetchJson } = useFetch();

export function useSwal() {
	const swal = inject('$swal')
	return { 
		successAlert: (string) => swal.fire({
			text: string,
			timerProgressBar: true,
			timer: 3000,
		  	icon: "success",
		  	confirmButtonText: "Close",
		  	buttonsStyling: false,
		  	reverseButtons: true,
		  	customClass: {
		  		popup: 'rounded-4',
		  		confirmButton: 'btn py-2 px-3 btn-dark',
		  	}
		}),

		successConfirm: (string, confirmBtnText) => swal.fire({
			text: string,
			timer: 15000,
			timerProgressBar: true,
			showCancelButton: true,
		  	icon: "success",
		  	confirmButtonText: confirmBtnText || "Back",
		  	cancelButtonText: "Close",
		  	buttonsStyling: false,
		  	customClass: {
		  		popup: 'rounded-4',
		  		confirmButton: 'btn py-2 px-3 btn-dark rounded-end-0',
		  		cancelButton: 'btn py-2 px-3 btn-secondary rounded-start-0',
		  	}
		}),

		errorAlert: (string) => swal.fire({
			text: string,
			timerProgressBar: true,
			allowOutsideClick: false,
			timer: 10000,
		  	icon: "error",
		  	confirmButtonText: "Close",
		  	buttonsStyling: false,
		  	reverseButtons: true,
		  	customClass: {
		  		popup: 'rounded-4',
		  		confirmButton: 'btn py-2 px-3 btn-dark',
		  	}
		}),

		warningAlert: (string) => swal.fire({
			text: string,
			timerProgressBar: true,
			timer: 5000,
		  	icon: "warning",
		  	confirmButtonText: "Close",
		  	buttonsStyling: false,
		  	reverseButtons: true,
		  	customClass: {
		  		popup: 'rounded-4',
		  		confirmButton: 'btn py-2 px-3 btn-dark',
		  	}
		}),

		confirm: (string, params = {}) => swal.fire({
			text: string,
			showCancelButton: true,
		  	confirmButtonText: "Confirm",
		  	cancelButtonText: "Close",
		  	buttonsStyling: false,
		  	reverseButtons: true,
		  	customClass: {
		  		popup: 'rounded-4',
		  		confirmButton: 'btn px-3 btn-dark rounded-start-0',
		  		cancelButton: 'btn px-3 btn-secondary rounded-end-0',
		  	},
		  	...params
		}),

		noteAlert: (transcriptId) => swal.fire({
			allowEscapeKey: false,
			title: "Add new note",
			html: `
			    <div class="mb-3 text-start">
	                <label class="mb-1 font-size-0-8rem text-uppercase">Category</label>
	                <select id="swal-note-category" name="name" class="form-select" style="box-shadow: none !important;">
	                    <option value="prompt">Prompt</option>
	                    <option value="system">System</option>
	                    <option value="both">Both</option>
	                </select>
	            </div>

	            <div class="mb-3 text-start">
	                <label class="mb-1 font-size-0-8rem text-uppercase">Content</label>
	                <textarea id="swal-note-content" class="form-control" style="min-height:200px; box-shadow: none !important;" rows="4" cols="30"></textarea>
	            </div>
			`,
			focusConfirm: false,
			buttonsStyling: false,
		  	reverseButtons: true,
		  	showCancelButton: true,
		  	showLoaderOnConfirm: true,
		  	confirmButtonText: "Save",
		  	cancelButtonText: "Close",
		  	customClass: {
		  		popup: 'rounded-4',
		  		confirmButton: 'btn px-3 btn-success rounded-start-0',
		  		cancelButton: 'btn px-3 btn-secondary rounded-end-0',
		  	},
			preConfirm: async () => {

				let value = {
			    	name: document.getElementById("swal-note-category").value,
			      	content: document.getElementById("swal-note-content").value,
			      	transcript_id: transcriptId
			    }

			    try {

					let response = await fetchJson(`/customer/transcript-notes`, {
						method: 'POST',
						body: value
					})
					
				    if (response.errors) {
				    	throw Error(Object.values(response.errors)[0]);
				    };

				    if (response.message) {
				    	throw Error(response.message);
				    };

				    return value;
			    } catch (error) {
			    	swal.showValidationMessage(`
				        ${error.message}
				    `);
			    }
			}
		}).then(result => {
			if (result.isConfirmed) {
				swal.fire({
					text: "Note added successfully",
					timer: 10000,
					timerProgressBar: true,
					showCancelButton: false,
				  	icon: "success",
				  	buttonsStyling: false,
				  	customClass: {
				  		popup: 'rounded-4',
				  		confirmButton: 'btn py-2 px-3 btn-dark rounded',
				  		cancelButton: 'btn py-2 px-3 btn-secondary rounded',
				  	}
				})
			}
		})
	}
}
