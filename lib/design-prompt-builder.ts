export const GLOBAL_PERSPECTIVE_LOCK_INSTRUCTION =
  "PERSPECTIVE LOCK: The output image MUST preserve the exact camera angle, horizon line, floor level, ceiling height, vanishing points, and focal length of the source photo. Do not shift, tilt, rotate, or rescale the architectural geometry.";

export const GLOBAL_MASTERPIECE_QUALITY_INSTRUCTION =
  "Generate a photorealistic, magazine-grade architectural rendering with accurate lighting, material fidelity, and high-end finish quality. The result must look indistinguishable from a professional interior design photograph.";

export const ARCHITECTURAL_MATERIAL_REFLECTION_INSTRUCTION =
  "Material behavior: accurately render light interaction with every surface — diffuse scatter on matte walls, specular highlights on polished stone, anisotropic reflections on brushed metal, caustic patterns through glass, subsurface scattering on translucent fabrics, and ambient occlusion at junctions.";

export const GLOBAL_REALISM_TOKEN_INJECTION =
  "photorealistic, 8K UHD, architectural photography, natural lighting, accurate shadows, high dynamic range, material fidelity, luxury finish, professional interior design, magazine quality, DSLR quality, soft bokeh, correct proportions, realistic reflections";

const SERVICE_TYPE_INSTRUCTIONS: Record<string, string> = {
  paint:
    "Apply the selected paint color or wall finish to the designated wall surfaces only. Preserve original lighting direction, furniture shadows on walls, window light, trim, moldings, and all non-wall elements.",
  floor:
    "Apply the selected flooring material to the floor area only. Ensure wood grain, marble veining, tile grout lines, and material scale follow the room's perspective. Preserve walls, furniture grounding shadows, and baseboards.",
  redesign:
    "Completely redesign the interior, exterior, or garden space with luxury materials, refined furniture, and premium finishes. Maintain the original structural geometry, window positions, door openings, and camera perspective.",
  layout:
    "Reconfigure the furniture layout and spatial flow to maximize openness, circulation, and visual balance. Keep walls, windows, doors, and fixed architectural elements in their original positions.",
  replace:
    "Replace the specified object or area with a premium alternative. Ensure the replacement integrates naturally with surrounding materials, lighting, and perspective.",
};

const ROOM_TYPE_LABELS: Record<string, string> = {
  "living room": "Living Room",
  "bedroom": "Bedroom",
  "kitchen": "Kitchen",
  "bathroom": "Bathroom",
  "dining room": "Dining Room",
  "office": "Home Office",
  "exterior": "Exterior Facade",
  "garden": "Garden & Landscape",
  "facade": "Building Facade",
};

function labelForRoomType(roomType: string): string {
  const lower = roomType.toLowerCase();
  for (const [key, label] of Object.entries(ROOM_TYPE_LABELS)) {
    if (lower.includes(key)) return label;
  }
  return roomType.charAt(0).toUpperCase() + roomType.slice(1);
}

export function buildDesignPrompt(args: {
  serviceType: string;
  roomType: string;
  style: string;
  styleSelections?: string[];
  colorPalette: string;
  customPrompt?: string;
  targetColor?: string;
  targetColorCategory?: string;
  targetSurface?: string;
  aspectRatio?: string;
  regenerate?: boolean;
  smartSuggest?: boolean;
}): string {
  const serviceInstruction = SERVICE_TYPE_INSTRUCTIONS[args.serviceType] ?? SERVICE_TYPE_INSTRUCTIONS.redesign;
  const roomLabel = labelForRoomType(args.roomType);

  const segments: string[] = [];

  segments.push(
    `Transform this ${roomLabel.toLowerCase()} with a ${args.style} design direction.`,
  );

  if (args.colorPalette && args.colorPalette.toLowerCase() !== "surprise") {
    segments.push(`Color palette: ${args.colorPalette}.`);
  }

  if (args.targetColor) {
    segments.push(`Target color: ${args.targetColor}.`);
  }

  if (args.targetColorCategory) {
    segments.push(`Color category: ${args.targetColorCategory}.`);
  }

  if (args.targetSurface) {
    segments.push(`Surface material: ${args.targetSurface}.`);
  }

  segments.push(serviceInstruction);

  if (args.customPrompt) {
    segments.push(args.customPrompt);
  }

  if (args.styleSelections && args.styleSelections.length > 1) {
    segments.push(
      `Style fusion: blend ${args.styleSelections.join(", ")} into a cohesive architectural direction.`,
    );
  }

  segments.push(GLOBAL_PERSPECTIVE_LOCK_INSTRUCTION);
  segments.push(GLOBAL_MASTERPIECE_QUALITY_INSTRUCTION);

  if (args.aspectRatio) {
    segments.push(`Aspect ratio: ${args.aspectRatio}.`);
  }

  if (args.regenerate) {
    segments.push("This is a regeneration — refine the previous output with sharper detail and improved material accuracy.");
  }

  if (args.smartSuggest) {
    segments.push("AI Suggest mode: deliver a professionally balanced, high-end design that feels curated rather than extreme.");
  }

  return segments.join(" ");
}

export function buildDesignNegativePrompt(args: {
  serviceType: string;
  roomType: string;
}): string {
  const common = [
    "text",
    "watermark",
    "labels",
    "captions",
    "logos",
    "split layout",
    "before-and-after comparison",
    "blurry",
    "distorted proportions",
    "unrealistic lighting",
    "floating furniture",
    "clipped objects",
    "warped architecture",
    "shifted perspective",
    "changed camera angle",
    "altered floor level",
    "changed ceiling height",
  ];

  if (args.serviceType === "paint") {
    common.push(
      "changed furniture",
      "modified flooring",
      "altered ceiling",
      "removed decor",
      "new windows",
      "structural changes",
    );
  }

  if (args.serviceType === "floor") {
    common.push(
      "changed walls",
      "modified furniture",
      "altered ceiling",
      "removed decor",
      "structural changes",
    );
  }

  return common.join(", ");
}

export function normalizeAspectRatio(aspectRatio: string | undefined | null): string {
  if (!aspectRatio || typeof aspectRatio !== "string") return "1:1";

  const normalized = aspectRatio.trim().toLowerCase();

  const aspectMap: Record<string, string> = {
    "1:1": "1:1",
    "square": "1:1",
    "4:3": "4:3",
    "3:4": "3:4",
    "portrait": "3:4",
    "16:9": "16:9",
    "9:16": "9:16",
    "landscape": "16:9",
    "wide": "16:9",
    "tall": "9:16",
    "3:2": "3:2",
    "2:3": "2:3",
  };

  return aspectMap[normalized] ?? "1:1";
}
