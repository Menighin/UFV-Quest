from UFVQuestAPI.models import User
from django.core.exceptions import ObjectDoesNotExist

def authorize(fb_id, api_key):
	try:
		u = User.objects.get(facebook_id = fb_id)
		if (u.api_key == api_key):
			return True
		else:
			return False
	except ObjectDoesNotExist:
		return False
