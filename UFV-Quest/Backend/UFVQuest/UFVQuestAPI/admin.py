from django.contrib import admin
from UFVQuestAPI.models import User, QuestType, GoToAndAnswer, SeekAndAnswer

class UserAdmin(admin.ModelAdmin):
	list_display = ['name']


admin.site.register(User, UserAdmin)
admin.site.register(SeekAndAnswer)
admin.site.register(GoToAndAnswer)
admin.site.register(QuestType)
