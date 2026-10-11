#pragma once

struct OverlaySettings {
    bool showBoxes = true;
    bool showNames = true;
    bool showDistance = true;
    bool showRadar = false;
    float maxDistance = 300.0f;
};
EOFcat > src/overlay.cpp <<'EOF'
#include "overlay.h"

const char* GetOverlayStatus(const OverlaySettings& settings) {
    return settings.showNames ? "Names: ON" : "Names: OFF";
}
