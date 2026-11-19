package com.swe401.safeready.data

import com.swe401.safeready.R

/**
 * All lesson, exercise and checklist content used by the app.
 * Safety actions are based on the FEMA Ready.gov "Be a Hero" youth curriculum (grades 9-12).
 */
object ContentRepository {

    val categories: List<DisasterCategory> = listOf(
        DisasterCategory(
            id = "flood",
            name = "Flood",
            emoji = "🌧️",
            colorRes = R.color.cat_flood,
            illustrationRes = R.drawable.illus_flood,
            summary = "Floods are the most common disaster in Malaysia, especially during the monsoon season. " +
                    "Flash floods can rise within minutes, so knowing when to move to higher ground saves lives.",
            before = listOf(
                "Find out if your home or school is in a flood-prone area.",
                "Pack a Go-Bag with water, food, a torch and important documents in a waterproof bag.",
                "Agree on a family meeting place on higher ground.",
                "Follow weather warnings on the radio, TV or official apps."
            ),
            during = listOf(
                "Move to higher ground immediately if flash flooding is possible.",
                "Never walk, swim or cycle through moving water. About 15 cm of fast water can knock you down.",
                "Help move important items to an upper floor if there is time.",
                "Follow instructions from officials and evacuate when told."
            ),
            after = listOf(
                "Return home only when authorities say it is safe.",
                "Stay away from floodwater. It may be dirty or hide sharp objects and live wires.",
                "Do not touch electrical equipment if it is wet or you are standing in water.",
                "Throw away food that has touched floodwater."
            ),
            videoSearch = "flood safety tips for students",
            guideUrl = "https://www.ready.gov/floods",
            badgeTitle = "Flood Ready",
            questions = listOf(
                Question(
                    "Heavy rain has been falling all day and a flash flood warning is announced. What should you do first?",
                    listOf("Go outside to take photos", "Move to higher ground", "Wait until the water reaches the door", "Go to the basement"),
                    1,
                    "Flash floods rise fast. Moving to higher ground right away is the safest action."
                ),
                Question(
                    "On your way home you reach a road covered by fast-moving water. What do you do?",
                    listOf("Walk through slowly", "Ride your bicycle across quickly", "Turn around and find another route", "Hold a friend's hand and cross"),
                    2,
                    "Moving water only 15 cm deep can knock you over. Turn around, don't drown."
                ),
                Question(
                    "Which item is most important to keep in a waterproof bag in your Go-Bag?",
                    listOf("Copies of IC and important documents", "Video game console", "School textbooks", "Extra shoes"),
                    0,
                    "Identity documents are needed for help and relief after a disaster, so keep copies dry."
                ),
                Question(
                    "The flood has gone down. When can your family return home?",
                    listOf("As soon as the rain stops", "When authorities say it is safe", "The next morning", "When neighbours return"),
                    1,
                    "Buildings, roads and electricity may still be dangerous. Wait for an official all-clear."
                ),
                Question(
                    "After a flood, you find canned food and bread that touched floodwater. What should you do?",
                    listOf("Eat it quickly", "Wash and eat all of it", "Throw away the food that touched floodwater", "Give it to pets"),
                    2,
                    "Floodwater carries sewage and germs. Food that touched it is not safe to eat."
                )
            )
        ),
        DisasterCategory(
            id = "fire",
            name = "Fire",
            emoji = "🔥",
            colorRes = R.color.cat_fire,
            illustrationRes = R.drawable.illus_fire,
            summary = "A small fire can become life-threatening in just a few minutes. " +
                    "Smoke is often more dangerous than flames, so getting out quickly is the top priority.",
            before = listOf(
                "Make sure there is a working smoke alarm and test it monthly.",
                "Know two ways out of every room.",
                "Practise a fire escape drill at home twice a year.",
                "Never leave cooking, candles or mosquito coils unattended."
            ),
            during = listOf(
                "When the alarm sounds, get out fast. You may have only seconds.",
                "Stay low and crawl under the smoke to the exit.",
                "Feel a door with the back of your hand before opening it. If it is hot, use your second way out.",
                "If your clothes catch fire: Stop, Drop and Roll."
            ),
            after = listOf(
                "Once you are out, stay out. Never go back inside for belongings or pets.",
                "Call the fire brigade (999 or 994 in Malaysia) and go to your family meeting point.",
                "Tell firefighters if anyone or any pet is still inside.",
                "Enter the building again only when firefighters say it is safe."
            ),
            videoSearch = "home fire safety escape tips for students",
            guideUrl = "https://www.ready.gov/home-fires",
            badgeTitle = "Fire Smart",
            questions = listOf(
                Question(
                    "The smoke alarm goes off at night and the room is filling with smoke. How should you move?",
                    listOf("Run standing up", "Crawl low under the smoke", "Hide under the bed", "Open all the windows first"),
                    1,
                    "Cleaner, cooler air is near the floor. Crawl low to the nearest exit."
                ),
                Question(
                    "You reach a closed door and the handle feels hot. What do you do?",
                    listOf("Open it quickly", "Kick it open", "Use your second way out", "Wait for the fire to pass"),
                    2,
                    "A hot door means fire is on the other side. Use another exit."
                ),
                Question(
                    "Your sleeve catches fire. What is the correct action?",
                    listOf("Run to find water", "Stop, Drop and Roll", "Wave your arm to put it out", "Take off your shirt while running"),
                    1,
                    "Running feeds the flames with oxygen. Stop, drop to the ground and roll to smother the fire."
                ),
                Question(
                    "You escaped but your phone is still inside. What should you do?",
                    listOf("Run back in quickly", "Ask a friend to get it", "Stay outside and let firefighters handle it", "Go back in through a window"),
                    2,
                    "Get out and stay out. No belonging is worth your life."
                ),
                Question(
                    "How often should a family practise its home fire escape plan?",
                    listOf("Never", "Only after a fire", "Twice a year", "Once every five years"),
                    2,
                    "FEMA recommends practising your escape plan at least twice a year."
                )
            )
        ),
        DisasterCategory(
            id = "earthquake",
            name = "Earthquake",
            emoji = "🌍",
            colorRes = R.color.cat_earthquake,
            illustrationRes = R.drawable.illus_earthquake,
            summary = "Earthquakes happen without warning. Malaysia feels tremors from nearby regions and Sabah has active faults. " +
                    "The safest response is Drop, Cover and Hold On.",
            before = listOf(
                "Secure heavy furniture and shelves to walls.",
                "Identify safe spots in each room, such as under a sturdy table.",
                "Keep a torch and shoes near your bed.",
                "Practise Drop, Cover and Hold On drills at school and home."
            ),
            during = listOf(
                "Indoors: Drop to the ground, take Cover under a sturdy table and Hold On.",
                "Stay away from windows, glass and things that could fall.",
                "Do not use the lift.",
                "Outdoors: move to an open area away from buildings, streetlights and power lines."
            ),
            after = listOf(
                "Expect aftershocks and Drop, Cover and Hold On again if shaking starts.",
                "If trapped, cover your mouth and tap on a pipe or wall instead of shouting.",
                "Check yourself and others for injuries.",
                "Leave damaged buildings and stay out until they are inspected."
            ),
            videoSearch = "earthquake drop cover hold on for students",
            guideUrl = "https://www.ready.gov/earthquakes",
            badgeTitle = "Quake Safe",
            questions = listOf(
                Question(
                    "You are in class when the ground starts shaking. What do you do?",
                    listOf("Run out of the building", "Drop, Cover and Hold On under your desk", "Stand near the window", "Take the lift downstairs"),
                    1,
                    "Most injuries come from falling objects. Drop, Cover and Hold On protects you."
                ),
                Question(
                    "You are outside in a field during an earthquake. Where should you go?",
                    listOf("Into the nearest building", "Under a tree", "An open area away from buildings and power lines", "Next to a wall"),
                    2,
                    "Outdoors, the danger is falling debris. Stay in the open, away from structures."
                ),
                Question(
                    "Why should you NOT use the lift during or right after an earthquake?",
                    listOf("It is slower than stairs", "Power may fail and trap you", "Lifts are only for teachers", "It uses too much electricity"),
                    1,
                    "Power cuts and damage can stop the lift and trap you inside."
                ),
                Question(
                    "You are trapped under debris after an earthquake. How should you signal for help?",
                    listOf("Shout continuously", "Light a match", "Tap on a pipe or wall", "Try to dig out quickly"),
                    2,
                    "Shouting makes you breathe in dust and tires you. Tapping helps rescuers find you."
                ),
                Question(
                    "The shaking has stopped. What should you expect next?",
                    listOf("Nothing, it is over", "Aftershocks", "A flood", "Snow"),
                    1,
                    "Aftershocks are common. Be ready to Drop, Cover and Hold On again."
                )
            )
        ),
        DisasterCategory(
            id = "landslide",
            name = "Landslide",
            emoji = "⛰️",
            colorRes = R.color.cat_landslide,
            illustrationRes = R.drawable.illus_landslide,
            summary = "Landslides often follow heavy rain on hillsides and slopes. " +
                    "Spotting the warning signs early gives you time to get out of the way.",
            before = listOf(
                "Learn whether your area has steep slopes or past landslides.",
                "Watch for warning signs: new cracks in the ground, tilting trees or poles, and muddy streams.",
                "Plan an evacuation route away from slopes and valleys.",
                "Pay attention to heavy rain warnings."
            ),
            during = listOf(
                "Move quickly away from the path of the landslide.",
                "Avoid river valleys and low areas where debris flows.",
                "Listen for rumbling sounds or cracking trees.",
                "If you cannot escape, curl into a tight ball and protect your head."
            ),
            after = listOf(
                "Stay away from the slide area. More slides can happen.",
                "Watch for flooding, which can follow a landslide.",
                "Report broken power lines and damaged roads to authorities.",
                "Help others only if it is safe to do so."
            ),
            videoSearch = "landslide safety tips for students",
            guideUrl = "https://www.ready.gov/landslides-debris-flow",
            badgeTitle = "Slope Guard",
            questions = listOf(
                Question(
                    "Which of these is a warning sign of a possible landslide?",
                    listOf("Clear blue sky", "New cracks in the ground and tilting trees", "Birds singing", "A cool breeze"),
                    1,
                    "Cracks, tilting trees or poles and sudden muddy water are signs the slope is moving."
                ),
                Question(
                    "You hear a loud rumbling from the hill above after days of heavy rain. What do you do?",
                    listOf("Go closer to look", "Move quickly away from the slope's path", "Stay inside the valley", "Record a video"),
                    1,
                    "Get out of the path immediately. Debris can move faster than you can run downhill."
                ),
                Question(
                    "Where is it most dangerous to be during a debris flow?",
                    listOf("On high, flat ground", "In river valleys and low areas", "In an open field far from hills", "In a sturdy building far from slopes"),
                    1,
                    "Debris flows follow valleys and channels, so low areas near slopes are most dangerous."
                ),
                Question(
                    "You cannot escape an approaching landslide. What should you do?",
                    listOf("Lie flat on your back", "Run downhill", "Curl into a tight ball and protect your head", "Climb a tree"),
                    2,
                    "Protecting your head gives you the best chance of avoiding serious injury."
                ),
                Question(
                    "What can often happen after a landslide?",
                    listOf("Flooding and more slides", "Earthquakes", "Snowfall", "Nothing else"),
                    0,
                    "Landslides can block rivers and cause floods, and the slope may slide again."
                )
            )
        ),
        DisasterCategory(
            id = "tsunami",
            name = "Tsunami",
            emoji = "🌊",
            colorRes = R.color.cat_tsunami,
            illustrationRes = R.drawable.illus_tsunami,
            summary = "A tsunami is a series of huge waves usually caused by an undersea earthquake. " +
                    "The 2004 Indian Ocean tsunami reached Penang and Kedah. Nature gives warning signs, so act fast.",
            before = listOf(
                "Know if your home, school or holiday spot is in a coastal tsunami zone.",
                "Learn the evacuation routes and signs to higher ground.",
                "Plan to reach a place at least 30 metres above sea level or 3 km inland.",
                "Keep your Go-Bag ready if you live near the coast."
            ),
            during = listOf(
                "If you feel a strong earthquake near the coast, move inland and uphill immediately.",
                "If the sea suddenly draws back and exposes the seabed, run to higher ground.",
                "Go on foot if possible. Roads may be jammed.",
                "Never go to the beach to watch the waves."
            ),
            after = listOf(
                "Stay on high ground. More waves can arrive for hours.",
                "Return only when officials say it is safe.",
                "Stay away from damaged buildings and debris in the water.",
                "Use text messages instead of calls to keep phone lines free."
            ),
            videoSearch = "tsunami safety warning signs for students",
            guideUrl = "https://www.ready.gov/tsunamis",
            badgeTitle = "Wave Watcher",
            questions = listOf(
                Question(
                    "At the beach, the sea suddenly pulls far back and exposes the seabed. What does it mean?",
                    listOf("Low tide, good time to collect shells", "A tsunami may be coming", "The weather will be sunny", "A storm is over"),
                    1,
                    "A receding shoreline is a natural warning sign of a tsunami. Move to high ground now."
                ),
                Question(
                    "Where should you go when a tsunami warning is issued?",
                    listOf("To the beach to watch", "Inland and to higher ground", "Into the sea on a boat", "Ground floor of a beach hotel"),
                    1,
                    "Get as high and as far inland as you can."
                ),
                Question(
                    "The first wave has passed. Is it safe to go back to the shore?",
                    listOf("Yes, it is over", "No, more waves can follow for hours", "Yes, but only to look", "Only if the sun is out"),
                    1,
                    "Tsunamis come as a series of waves and later waves can be bigger."
                ),
                Question(
                    "You feel a strong earthquake while at a coastal town. What is your next action?",
                    listOf("Wait for an official warning before moving", "Move inland and uphill immediately", "Go swimming", "Go to the harbour"),
                    1,
                    "A strong coastal earthquake is itself a warning. Don't wait, move to high ground."
                ),
                Question(
                    "Why is it better to send a text than make a call after a disaster?",
                    listOf("Texts are free", "It keeps phone lines free for emergency calls", "Calls do not work at the beach", "Texts are more fun"),
                    1,
                    "Networks get overloaded. Texts use less capacity and leave lines open for emergencies."
                )
            )
        ),
        DisasterCategory(
            id = "storm",
            name = "Thunderstorm",
            emoji = "⛈️",
            colorRes = R.color.cat_storm,
            illustrationRes = R.drawable.illus_storm,
            summary = "Malaysia has one of the highest rates of lightning in the world. " +
                    "Thunderstorms also bring strong winds and flash floods.",
            before = listOf(
                "Check the weather forecast before outdoor activities.",
                "Remember: when thunder roars, go indoors.",
                "Secure or bring in loose outdoor items that wind could blow away.",
                "Keep a torch and a charged power bank ready in case of blackouts."
            ),
            during = listOf(
                "Use the 30/30 rule: go indoors if thunder follows lightning within 30 seconds.",
                "Avoid open fields, hilltops, tall isolated trees and water.",
                "Indoors, avoid using corded phones, plugged-in electronics and running water.",
                "Stay away from windows and doors."
            ),
            after = listOf(
                "Wait 30 minutes after the last thunder before going outside.",
                "Stay away from fallen power lines and report them.",
                "Watch for flash flooding in low areas.",
                "If someone is struck by lightning, call 999. It is safe to touch them and give first aid."
            ),
            videoSearch = "lightning thunderstorm safety tips for students",
            guideUrl = "https://www.ready.gov/thunderstorms-lightning",
            badgeTitle = "Storm Spotter",
            questions = listOf(
                Question(
                    "You see lightning and hear thunder 10 seconds later while playing football. What do you do?",
                    listOf("Keep playing until it rains", "Go indoors immediately", "Stand under a tall tree", "Lie down in the middle of the field"),
                    1,
                    "Thunder within 30 seconds of lightning means the storm is close enough to strike. Go indoors."
                ),
                Question(
                    "How long should you wait after the last thunder before going outside again?",
                    listOf("5 minutes", "30 minutes", "Until the rain stops", "No need to wait"),
                    1,
                    "The 30/30 rule says to wait 30 minutes after the last thunder."
                ),
                Question(
                    "Which activity should you avoid indoors during a thunderstorm?",
                    listOf("Reading a book", "Taking a shower", "Talking to family", "Eating dinner"),
                    1,
                    "Lightning can travel through plumbing, so avoid running water during a storm."
                ),
                Question(
                    "Where is the most dangerous place to shelter during a lightning storm?",
                    listOf("Inside a building", "Inside a hard-top car", "Under a tall isolated tree", "In a low area away from trees"),
                    2,
                    "Tall isolated objects attract lightning. Never shelter under a lone tree."
                ),
                Question(
                    "After the storm you see a fallen power line on the road. What should you do?",
                    listOf("Move it with a stick", "Step over it carefully", "Stay far away and report it", "Touch it to check if it is live"),
                    2,
                    "Fallen lines may still be live. Keep away and report it to the authorities."
                )
            )
        )
    )

    val kitItems: List<KitItem> = listOf(
        KitItem("water", "Drinking water", "At least 1 bottle per person for each day"),
        KitItem("food", "Non-perishable food", "Biscuits, canned food, energy bars"),
        KitItem("torch", "Torch with spare batteries", "Avoid candles during disasters"),
        KitItem("whistle", "Whistle", "To signal for help"),
        KitItem("firstaid", "First aid kit", "Plasters, bandages, antiseptic"),
        KitItem("medicine", "Personal medicine", "Enough for a few days"),
        KitItem("documents", "Copies of IC and documents", "Sealed in a waterproof bag"),
        KitItem("powerbank", "Charged power bank and cable", "Keep your phone working"),
        KitItem("cash", "Some cash", "ATMs may not work"),
        KitItem("clothes", "Change of clothes", "Plus a raincoat"),
        KitItem("masks", "Face masks", "For smoke, dust and haze"),
        KitItem("contacts", "Emergency contact list", "Written on paper, not only in your phone")
    )

    val dailyTips: List<String> = listOf(
        "Turn around, don't drown. Never walk through moving floodwater.",
        "Know two ways out of every room in your home.",
        "Drop, Cover and Hold On when the ground shakes.",
        "When thunder roars, go indoors.",
        "If the sea suddenly draws back, run to higher ground.",
        "Keep a whistle in your Go-Bag to signal for help.",
        "Text, don't call, after a disaster to keep lines free.",
        "In Malaysia, call 999 for police, ambulance and fire emergencies."
    )

    fun getCategory(id: String?): DisasterCategory? = categories.find { it.id == id }
}
